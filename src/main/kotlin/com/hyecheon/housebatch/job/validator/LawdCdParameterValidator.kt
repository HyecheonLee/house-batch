package com.hyecheon.housebatch.job.validator

import com.hyecheon.housebatch.job.Constant
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator


/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/02
 */
class LawdCdParameterValidator : JobParametersValidator {
	override fun validate(parameters: JobParameters?) {
		val lawdCd = parameters.validParameter(Constant.LawdCd)
		if (lawdCd.length != 5) throw JobParametersInvalidException("${Constant.YEAR_MONTH} 문자열이 5자리여야 합니다.")
	}
}