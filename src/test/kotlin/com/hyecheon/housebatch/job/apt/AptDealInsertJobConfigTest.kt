package com.hyecheon.housebatch.job.apt

import com.hyecheon.housebatch.BatchTestConfig
import com.hyecheon.housebatch.adapter.ApartmentApiResource
import com.hyecheon.housebatch.core.service.AptDealService
import com.hyecheon.housebatch.core.service.LawdService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/11
 */
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [AptDealInsertJobConfig::class])
internal class AptDealInsertJobConfigTest : BatchTestConfig() {

	@MockBean
	lateinit var aptDealService: AptDealService

	@MockBean
	lateinit var apartmentApiResource: ApartmentApiResource

	@MockBean
	lateinit var lawdService: LawdService

	@DisplayName("1. 성공")
	@Test
	internal fun test_1() {
		//given
		given(lawdService.guLawdCd()).will { listOf("41135") }
		given(apartmentApiResource.getResource(any(), any())).will {
			ClassPathResource("test.api-response.xml")
		}
		//when
		val execution = jobLauncherTestUtils.launchJob(JobParameters(mapOf("yearMonth" to JobParameter("2021-07"))))

		//then
		Assertions.assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
		verify(aptDealService, times(1)).save(any())

	}

	@DisplayName("2. yearMonth가 존재하지않을때 오류")
	@Test
	internal fun test_2() {
		//given
		given(lawdService.guLawdCd()).will { listOf("41135") }
		given(apartmentApiResource.getResource(any(), any())).will {
			ClassPathResource("test.api-response.xml")
		}

		//when
		assertThrows<JobParametersInvalidException> { jobLauncherTestUtils.launchJob() }

		//then
		verify(aptDealService, never()).save(any())
	}
}