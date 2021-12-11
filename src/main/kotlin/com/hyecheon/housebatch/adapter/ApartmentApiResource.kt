package com.hyecheon.housebatch.adapter

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/01
 */

/**
 * 아파트 실거래 API를 호출하기 위한 파라미터
 * 1. serviceKey - API 호출하기 위한 인증키
 * 2. LAWD_CD - 법정동 코드 10자리 중 앞 5자리 - 구 지역코드 guLawdCd. 예) 41135
 * 3. DEAL_YMD - 거래가 발생한 년월 예 20211201
 */
@Component
class ApartmentApiResource(
	@Value("\${external.apartment-api.path}")
	private val path: String,
	@Value("\${external.apartment-api.service-key}")
	private val serviceKey: String
) {

	private val log = LoggerFactory.getLogger(this::class.java)

	fun getResource(lawdCd: String, yearMonth: YearMonth): Resource = run {
		val url =
			"$path?serviceKey=$serviceKey&LAWD_CD=$lawdCd&DEAL_YMD=${yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"))}"
		log.info("Resource URL = {}", url)
		UrlResource(url)
	}
}