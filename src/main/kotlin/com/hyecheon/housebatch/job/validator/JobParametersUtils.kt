package com.hyecheon.housebatch.job.validator

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/02
 */

fun JobParameters?.validParameter(parameter: String) = run {
	val that = this ?: throw JobParametersInvalidException("JobParameters 가 null 입니다.")
	val result = that.getString(parameter) ?: throw JobParametersInvalidException("${parameter}가 존재하지 않습니다.")
	if (result.isBlank()) throw JobParametersInvalidException("${parameter}가 빈문자열입니다.")
	result
}