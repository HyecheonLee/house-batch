package com.hyecheon.housebatch.job.validator

import com.hyecheon.housebatch.job.Constant.DealDate
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/07
 */
class DealDateParameterValidator : JobParametersValidator {

	override fun validate(parameters: JobParameters?) {
		val dealDate = parameters.validParameter(DealDate)

		try {
			LocalDate.parse(dealDate)
		} catch (e: DateTimeParseException) {
			throw JobParametersInvalidException("$DealDate 가 올바른 날짜 형식이 아닙니다. yyyy-MM-dd이어야 합니다.")
		}

	}
}