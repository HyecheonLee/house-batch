package com.hyecheon.housebatch.job.apt

import com.hyecheon.housebatch.adapter.ApartmentApiResource
import com.hyecheon.housebatch.core.dto.AptDealDto
import com.hyecheon.housebatch.core.service.LawdService
import com.hyecheon.housebatch.job.Constant
import com.hyecheon.housebatch.job.validator.YearMonthParameterValidator
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.CompositeJobParametersValidator
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import java.time.YearMonth
import java.util.*


/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/29
 */
@Configuration
class AptDealInsertJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory,
	private val apartmentApiResource: ApartmentApiResource,
	private val lawdService: LawdService
) {

	@Bean
	fun aptDealInsertJob() = run {
		jobBuilderFactory.get("aptDealInsertJob")
			.incrementer(RunIdIncrementer())
			.validator(aptDealJobParameterValidator())
			.start(guLawdCdStep())
			.on("CONTINUABLE").to(aptDealInsertStep()).next(guLawdCdStep())
			.from(guLawdCdStep())
			.on("*").end()
			.end()
			.build()
	}

	private fun aptDealJobParameterValidator() = run {
		CompositeJobParametersValidator().apply {
			setValidators(listOf(YearMonthParameterValidator()))
		}
	}

	@JobScope
	@Bean
	fun guLawdCdStep() = run {
		stepBuilderFactory.get("guLawdCdStep")
			.tasklet(guLawdCdTasklet())
			.build()
	}

	@StepScope
	@Bean
	fun guLawdCdTasklet() = run {
		Tasklet { contribution, chunkContext ->
			val stepExecution = chunkContext.stepContext.stepExecution
			val executionContext = stepExecution.jobExecution.executionContext

			if (!executionContext.containsKey("guLawdCds")) {
				executionContext.put("guLawdCds", LinkedList(lawdService.guLawdCd()))
			}

			val queue: Queue<String> = executionContext.get("guLawdCds")!! as Queue<String>

			if (queue.isEmpty()) contribution.exitStatus = ExitStatus.COMPLETED
			else {
				executionContext.putString(Constant.guLawdCd, queue.poll())
				contribution.exitStatus = ExitStatus("CONTINUABLE")
			}

			RepeatStatus.FINISHED
		}
	}

	@JobScope
	@Bean
	fun aptDealInsertStep() = run {
		stepBuilderFactory["aptDealInsertStep"]
			.chunk<AptDealDto, AptDealDto>(1000)
			.reader(aptDealResourceReader())
			.writer(aptDealWriter())
			.build()
	}

	@StepScope
	@Bean
	fun aptDealResourceReader(
		@Value("#{jobExecutionContext['${Constant.guLawdCd}']}") lawdCd: String? = null,
		@Value("#{jobParameters['yearMonth']}") yearMonth: String? = null,
	) = run {
		StaxEventItemReaderBuilder<AptDealDto>().name("aptDealResourceReader")
			.resource(apartmentApiResource.getResource(lawdCd!!, YearMonth.parse(yearMonth)))
			.addFragmentRootElements("item")
			.unmarshaller(aptDealDtoMarshaller())
			.build()
	}

	@StepScope
	@Bean
	fun aptDealDtoMarshaller() =
		Jaxb2Marshaller().apply { setClassesToBeBound(AptDealDto::class.java) }

	@StepScope
	@Bean
	fun aptDealWriter() = run {
		ItemWriter<AptDealDto> {
			it.forEach { aptDealDto -> println(aptDealDto) }
		}
	}
}