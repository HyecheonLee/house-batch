package com.hyecheon.housebatch.core.entity

import com.hyecheon.housebatch.core.dto.AptDealDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/04
 */
@Entity
@Table(
	indexes = [Index(name = "idxJibunAptName", columnList = "jibun,apt_name", unique = true)],
)
@EntityListeners(AuditingEntityListener::class)
data class Apt(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(nullable = false, name = "apt_name")
	var aptName: String?,

	@Column(nullable = false, name = "jibun")
	var jibun: String?,

	@Column(nullable = false)
	var dong: String?,

	@Column(nullable = false)
	var guLawdCd: String?,

	@Column(nullable = false)
	var builtYear: Int?,

	@CreatedDate
	@Column(updatable = false)
	var createdAt: LocalDateTime = LocalDateTime.now(),

	@LastModifiedDate
	var updatedAt: LocalDateTime = LocalDateTime.now()
) {


	companion object {
		fun from(dto: AptDealDto) = run {
			Apt(
				aptName = dto.aptName?.trim(),
				jibun = dto.jibun?.trim(),
				dong = dto.dong?.trim(),
				guLawdCd = dto.regionalCode?.trim(),
				builtYear = dto.builtYear
			)
		}
	}
}