package com.hyecheon.housebatch.core.dto

import java.text.DecimalFormat

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/07
 */
data class AptDto(
	val name: String?,
	val price: Long?
) {
	companion object {
		private val decimalFormat = DecimalFormat()
	}

	fun toFormatPrice() = run {
		decimalFormat.format(price)
	}
}
