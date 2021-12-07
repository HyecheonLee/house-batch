package com.hyecheon.housebatch.core.dto

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/07
 */
data class NotificationDto(
	val email: String,
	val guName: String,
	val count: Int,
	val aptDeals: List<AptDto>
) {
	fun toMessage() = run {
		"""
		 $guName 아파트 실거래가 알람
		 총 ${count}개 거래가 발생했습니다.
		 ${aptDeals.joinToString("\n") { "- ${it.name} : ${it.toFormatPrice()}원" }}
		""".trimIndent()
	}
}