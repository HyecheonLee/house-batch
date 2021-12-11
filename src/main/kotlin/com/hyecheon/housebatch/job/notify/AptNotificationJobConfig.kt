package com.hyecheon.housebatch.job.notify

import com.hyecheon.housebatch.adapter.SendService
import com.hyecheon.housebatch.core.dto.NotificationDto
import com.hyecheon.housebatch.core.entity.AptNotification
import com.hyecheon.housebatch.core.repository.AptNotificationRepository
import com.hyecheon.housebatch.core.repository.LawdRepository
import com.hyecheon.housebatch.core.service.AptDealService
import com.hyecheon.housebatch.job.Constant
import com.hyecheon.housebatch.job.validator.DealDateParameterValidator
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import java.time.LocalDate

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/07
 */
@Configuration
class AptNotificationJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun aptNotificationJob(aptNotificationStep: TaskletStep) = run {
		jobBuilderFactory.get("aptNotificationJob")
			.incrementer(RunIdIncrementer())
			.start(aptNotificationStep)
			.validator(DealDateParameterValidator())
			.build()
	}

	@JobScope
	@Bean
	fun aptNotificationStep(
		aptNotificationProcessor: ItemProcessor<AptNotification, NotificationDto>,
		aptNotificationRepositoryItemReader: ItemReader<AptNotification>,
		aptNotificationWriter: ItemWriter<NotificationDto>
	) = run {
		stepBuilderFactory.get("aptNotificationStep")
			.chunk<AptNotification, NotificationDto>(10)
			.reader(aptNotificationRepositoryItemReader())
			.processor(aptNotificationProcessor)
			.writer(aptNotificationWriter)
			.build()
	}


	@StepScope
	@Bean
	fun aptNotificationRepositoryItemReader(
		aptNotificationRepository: AptNotificationRepository? = null
	) = run {
		RepositoryItemReaderBuilder<AptNotification>()
			.name("aptNotificationRepositoryItemReader")
			.repository(aptNotificationRepository!!)
			.methodName("findByEnabledIsTrue")
			.pageSize(10)
			.arguments(emptyList<Any>())
			.sorts(mapOf("aptNotificationId" to Sort.Direction.DESC))
			.build()
	}

	@StepScope
	@Bean
	fun aptNotificationProcessor(
		@Value("#{jobParameters['${Constant.DealDate}']}") dealDate: String? = null,
		aptDealService: AptDealService,
		lawdRepository: LawdRepository
	) = run {
		ItemProcessor<AptNotification, NotificationDto> { aptNotification ->
			val aptDtoList =
				aptDealService.findByGuLawdCdAndDealDate(aptNotification.guLawdCd, LocalDate.parse(dealDate))
			if (aptDtoList.isEmpty()) return@ItemProcessor null
			val lawd = lawdRepository.findByLawdCd(aptNotification.guLawdCd + "00000").orElseThrow()
			NotificationDto(
				email = aptNotification.email,
				guName = lawd.lawdDong!!,
				count = aptDtoList.size,
				aptDeals = aptDtoList,
			)
		}
	}

	@StepScope
	@Bean
	fun aptNotificationWriter(fakeSendService: SendService) = run {
		ItemWriter<NotificationDto> {
			it.forEach { notificationDto ->
				fakeSendService.send(notificationDto.email, notificationDto.toMessage())
			}
		}
	}
}