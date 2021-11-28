package com.hyecheon.housebatch.job.validator

import com.hyecheon.housebatch.job.Constant.FilePath
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import org.springframework.core.io.ClassPathResource

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
class FilePathParameterValidator : JobParametersValidator {


	override fun validate(parameters: JobParameters?) {

		val filePath = parameters?.getString(FilePath)
		if (filePath.isNullOrBlank()) {
			throw JobParametersInvalidException("${filePath}가 빈 문자열이거나 존재하지 않습니다.")
		}
		val resource = ClassPathResource(filePath)
		if (!resource.exists()) {
			throw JobParametersInvalidException("${filePath}가 class path에 존재하지 않습니다. 경로를 확인해주세요.")
		}
	}
}