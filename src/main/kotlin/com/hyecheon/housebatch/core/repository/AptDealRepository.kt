package com.hyecheon.housebatch.core.repository

import com.hyecheon.housebatch.core.entity.Apt
import com.hyecheon.housebatch.core.entity.AptDeal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/04
 */
interface AptDealRepository : JpaRepository<AptDeal, Long> {

	fun findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
		apt: Apt?, exclusiveArea: Double?, dealDate: LocalDate?, dealAmount: Long?, floor: Int?
	): Optional<AptDeal>

	@Query("select a  from AptDeal a join fetch a.apt where a.dealCanceled = false and a.dealDate = :dealDate and a.apt.guLawdCd = :guLawdCd")
	fun findAllByGuLawdCdAndDealDate(guLawdCd: String, dealDate: LocalDate): List<AptDeal>
}