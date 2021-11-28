package com.hyecheon.housebatch.core.service

import com.hyecheon.housebatch.core.entity.Lawd
import com.hyecheon.housebatch.core.repository.LawdRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
@Service
class LawdService(
	private val lawdRepository: LawdRepository
) {
	@Transactional
	fun save(lawd: Lawd) = run {
		val optionalLawd = lawdRepository.findByLawdCd(lawdCd = lawd.lawdCd)
		if (optionalLawd.isPresent) {
			val findLawd = optionalLawd.get()
			findLawd.update(lawd)
		} else {
			lawdRepository.save(lawd)
		}
	}

	@Transactional
	fun save(lawds: List<Lawd>) = run {
		val lawdCds = lawds.mapNotNull { lawd -> lawd.lawdCd }
		val savedLawds = lawdRepository.findAllByLawdCdIn(lawdCds)
		val groupByLawd = lawds.groupBy { lawd: Lawd ->
			savedLawds.any { savedLaw -> savedLaw.lawdCd == lawd.lawdCd }
		}
		groupByLawd[false]?.let { lawdRepository.saveAll(it) }
		groupByLawd[true]?.let {
			savedLawds.forEach { savedLawd ->
				it.find { it.lawdCd == savedLawd.lawdCd }?.let { savedLawd.update(it) }
			}
		}
		lawds
	}
}