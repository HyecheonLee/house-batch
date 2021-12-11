package com.hyecheon.housebatch.job.lawd

import com.hyecheon.housebatch.BatchTestConfig
import com.hyecheon.housebatch.core.entity.Lawd
import com.hyecheon.housebatch.core.service.LawdService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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
@ContextConfiguration(classes = [LawdInsertJobConfig::class])
internal class LawdInsertJobConfigTest : BatchTestConfig() {

	@MockBean
	lateinit var lawdService: LawdService

	@DisplayName("1. 성공")
	@Test
	internal fun test_1() {
		//given

		//when
		val parameters = JobParameters(mapOf("filePath" to JobParameter("TEST_LAWD_CODE.txt")))
		val execution = jobLauncherTestUtils.launchJob(parameters)

		//then
		assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
		verify(lawdService, times(1)).save(any<List<Lawd>>())
	}


	@DisplayName("2. 파일을 찾을수 없을때 실패")
	@Test
	internal fun test_2() {

		//when
		val parameters = JobParameters(mapOf("filePath" to JobParameter("NOT_EXIST_FILE.txt")))

		//then
		assertThatThrownBy { jobLauncherTestUtils.launchJob(parameters) }
			.isInstanceOf(JobParametersInvalidException::class.java)

		verify(lawdService, never()).save(any<List<Lawd>>())

	}
}