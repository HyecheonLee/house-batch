package com.hyecheon.housebatch.job.apt

import com.hyecheon.housebatch.core.dto.AptDealDto
import com.hyecheon.housebatch.job.Constant
import com.hyecheon.housebatch.job.validator.FilePathParameterValidator
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.oxm.jaxb.Jaxb2Marshaller


/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/29
 */
@Configuration
class AptDealInsertJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory
) {

	@Bean
	fun aptDealInsertJob() = run {
		jobBuilderFactory.get("aptDealInsertJob")
			.incrementer(RunIdIncrementer())
			.start(aptDealInsertStep())
			.validator(FilePathParameterValidator())
			.build()
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
	fun aptDealResourceReader(@Value("#{jobParameters['${Constant.FilePath}']}") filePath: String = "") = run {
		StaxEventItemReaderBuilder<AptDealDto>().name("aptDealResourceReader")
			.resource(ClassPathResource(filePath))
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