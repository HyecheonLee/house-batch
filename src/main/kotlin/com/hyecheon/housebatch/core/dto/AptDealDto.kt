package com.hyecheon.housebatch.core.dto

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/29
 */
@XmlRootElement(name = "item")
data class AptDealDto(
	@field:XmlElement(name = "거래금액")
	val _dealAmount: String? = null,

	@field:XmlElement(name = "건축년도")
	val builtYear: Int? = null,

	@field:XmlElement(name = "년")
	val year: Int? = null,

	@field:XmlElement(name = "법정동")
	val dong: String? = null,

	@field:XmlElement(name = "아파트")
	val aptName: String? = null,

	@field:XmlElement(name = "월")
	val month: Int? = null,

	@field:XmlElement(name = "일")
	val day: Int? = null,

	@field:XmlElement(name = "전용면적")
	val exclusiveArea: Double? = null,

	@field:XmlElement(name = "지번")
	val jibun: String? = null,

	@field:XmlElement(name = "지역코드")
	val regionalCode: String? = null,

	@field:XmlElement(name = "층")
	val floor: Int? = null,

	@field:XmlElement(name = "해제사유발생일")
	val _dealCanceledDate: String? = null,

	@field:XmlElement(name = "해제여부")
	val _dealCanceled: String? = null,
) {
	val dealAmount: Long?
		get() = run {
			_dealAmount?.replace(",", "")?.let {
				it.trim().toLong()
			}
		}

	val dealCanceled: Boolean
		get() = _dealCanceled == "O"
	val dealCanceledDate: LocalDate?
		get() = if (_dealCanceledDate.isNullOrBlank()) null else LocalDate.parse(
			_dealCanceledDate.trim(),
			DateTimeFormatter.ofPattern("yy.MM.dd")
		)


	fun getDealDate() = run {
		if (year != null && month != null && day != null) LocalDate.of(year, month, day) else null
	}
}