package com.hyecheon.housebatch.core.repository

import com.hyecheon.housebatch.core.entity.Lawd
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
interface LawdRepository : JpaRepository<Lawd, Long> {
	fun findByLawdCd(lawdCd: String?): Optional<Lawd>
	fun findAllByLawdCdIn(lawdCds: List<String>): List<Lawd>
}