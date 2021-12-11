package com.hyecheon.housebatch.job.notify

import com.hyecheon.housebatch.BatchTestConfig
import com.hyecheon.housebatch.adapter.FakeSendService
import com.hyecheon.housebatch.core.dto.AptDto
import com.hyecheon.housebatch.core.entity.AptNotification
import com.hyecheon.housebatch.core.entity.Lawd
import com.hyecheon.housebatch.core.repository.AptNotificationRepository
import com.hyecheon.housebatch.core.repository.LawdRepository
import com.hyecheon.housebatch.core.service.AptDealService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/11
 */

@ContextConfiguration(classes = [AptNotificationJobConfig::class])
internal class AptNotificationJobConfigTest : BatchTestConfig() {

	@Autowired
	lateinit var aptNotificationRepository: AptNotificationRepository

	@MockBean
	lateinit var aptDealService: AptDealService

	@MockBean
	lateinit var lawdRepository: LawdRepository

	@MockBean
	lateinit var fakeSendService: FakeSendService

	@AfterEach
	internal fun tearDown() {
		aptNotificationRepository.deleteAll()
	}

	@DisplayName("1. 성공")
	@Test
	internal fun test_1() {
		//given
		val dealDate = LocalDate.now().minusDays(1)
		givenAptNotification()
		givenLawdCd()
		givenAptDeal()

		//when
		val execution =
			jobLauncherTestUtils.launchJob(JobParameters(mapOf("dealDate" to JobParameter(dealDate.toString()))))
		//then

		Assertions.assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
		verify(fakeSendService, times(1)).send(any(), any())

	}

	fun givenAptNotification() = run {
		aptNotificationRepository.save(AptNotification(email = "abc@gamil.com", guLawdCd = "11110", enabled = true))
	}

	fun givenLawdCd() = run {
		given(lawdRepository.findByLawdCd("1111000000"))
			.will { Optional.of(Lawd(lawdCd = "1111000000", lawdDong = "경기도 성남시 분당구", exist = true)) }
	}

	fun givenAptDeal() = run {
		given(aptDealService.findByGuLawdCdAndDealDate("11110", LocalDate.now().minusDays(1)))
			.will {
				listOf(
					AptDto("IT아파트", 2000000000),
					AptDto("탄천아파트", 1500000000)
				)
			}
	}
}