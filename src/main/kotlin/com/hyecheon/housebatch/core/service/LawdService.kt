package com.hyecheon.housebatch.core.service

import com.hyecheon.housebatch.core.entity.Lawd
import com.hyecheon.housebatch.core.repository.LawdRepository
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement


/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
@Service
class LawdService(
	private val lawdRepository: LawdRepository,
	private val jdbcTemplate: JdbcTemplate
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
		val getSavedLawds =
			lawds.groupBy { lawd: Lawd -> savedLawds.any { savedLaw -> savedLaw.lawdCd == lawd.lawdCd } }
		getSavedLawds[true]?.let { updateLawds ->
			updateLawds.forEach { updateLawd ->
				savedLawds.find { savedLaw -> savedLaw.lawdCd == updateLawd.lawdCd }?.apply { update(updateLawd) }
			}
		}
		getSavedLawds[false]?.let { batchInsert(it) }
	}

	fun batchInsert(lawds: List<Lawd>) = run {
		jdbcTemplate.batchUpdate(
			"insert into lawd (lawd_cd, lawd_dong,exist,created_at, updated_at) values (?,?,?,now(),now())",
			object : BatchPreparedStatementSetter {
				override fun setValues(ps: PreparedStatement, i: Int) {
					ps.setString(1, lawds[i].lawdCd)
					ps.setString(2, lawds[i].lawdDong)
					ps.setBoolean(3, lawds[i].exist ?: false)
				}

				override fun getBatchSize() = lawds.size
			})
	}

}