package com.hyecheon.housebatch.core.repository

import com.hyecheon.housebatch.core.entity.Apt
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/04
 */
interface AptRepository : JpaRepository<Apt, Long> {
	fun findByJibunAndAndAptName(jubun: String?, aptName: String?): Optional<Apt>
}