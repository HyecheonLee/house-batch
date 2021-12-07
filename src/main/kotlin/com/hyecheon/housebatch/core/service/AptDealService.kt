package com.hyecheon.housebatch.core.service

import com.hyecheon.housebatch.core.dto.AptDealDto
import com.hyecheon.housebatch.core.dto.AptDto
import com.hyecheon.housebatch.core.entity.Apt
import com.hyecheon.housebatch.core.entity.AptDeal
import com.hyecheon.housebatch.core.repository.AptDealRepository
import com.hyecheon.housebatch.core.repository.AptRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/04
 */
@Service
class AptDealService(
	private val aptRepository: AptRepository,
	private val aptDealRepository: AptDealRepository
) {

	@Transactional
	fun save(dto: AptDealDto) = run {
		val apt = getAptOrNew(dto)
		saveApiDeal(dto, apt)
	}

	private fun saveApiDeal(dto: AptDealDto, apt: Apt) = run {
		val aptDeal = getAptDealOrNew(dto, apt)
		aptDeal.dealCanceled = dto.dealCanceled
		aptDeal.dealCanceledDate = dto.dealCanceledDate
	}

	private fun getAptDealOrNew(dto: AptDealDto, apt: Apt) = run {
		val optionalAptDeal = aptDealRepository
			.findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
				apt, dto.exclusiveArea, dto.getDealDate(), dto.dealAmount, dto.floor
			)
		if (optionalAptDeal.isPresent) optionalAptDeal.get()
		else aptDealRepository.save(AptDeal.from(dto, apt))
	}

	private fun getAptOrNew(dto: AptDealDto) = run {
		val optionalApt = aptRepository.findByJibunAndAndAptName(dto.jibun, dto.aptName)
		if (!optionalApt.isPresent) aptRepository.save(Apt.from(dto)) else optionalApt.get()
	}

	fun findByGuLawdCdAndDealDate(guLawdCd: String, dealDate: LocalDate) = run {
		aptDealRepository.findAllByGuLawdCdAndDealDate(guLawdCd, dealDate)
			.map { aptDeal ->
				AptDto(name = aptDeal.apt?.aptName, aptDeal.dealAmount)
			}
	}
}