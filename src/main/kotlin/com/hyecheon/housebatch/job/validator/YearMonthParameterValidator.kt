package com.hyecheon.housebatch.job.validator

import com.hyecheon.housebatch.job.Constant
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import java.time.YearMonth
import java.time.format.DateTimeParseException

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/02
 */
class YearMonthParameterValidator : JobParametersValidator {

	override fun validate(parameters: JobParameters?) {
		val yearMonth = parameters.validParameter(Constant.YEAR_MONTH)
		try {
			YearMonth.parse(yearMonth)
		} catch (e: DateTimeParseException) {
			throw JobParametersInvalidException("${yearMonth}은 잘못된 포멧입니다.")
		}
	}
}