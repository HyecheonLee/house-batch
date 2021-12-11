package com.hyecheon.housebatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/11
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
abstract class BatchTestConfig {
	@Autowired
	lateinit var jobLauncherTestUtils: JobLauncherTestUtils

}