package com.hyecheon.housebatch.core.entity

import com.hyecheon.housebatch.core.dto.AptDealDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/04
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
data class AptDeal(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val aptDealId: Long? = null,

	@ManyToOne
	@JoinColumn(name = "apt_id")
	var apt: Apt? = null,

	@Column(nullable = false)
	var exclusiveArea: Double?,

	@Column(nullable = false)
	var dealDate: LocalDate?,

	@Column(nullable = false)
	var dealAmount: Long?,

	@Column(nullable = false)
	var floor: Int?,

	@Column(nullable = false)
	var dealCanceled: Boolean = false,

	@Column
	var dealCanceledDate: LocalDate? = null,

	@CreatedDate
	@Column(updatable = false)
	var createdAt: LocalDateTime = LocalDateTime.now(),

	@LastModifiedDate
	var updatedAt: LocalDateTime = LocalDateTime.now()

) {

	companion object {
		fun from(dto: AptDealDto, apt: Apt?) = run {
			AptDeal(
				apt = apt,
				exclusiveArea = dto.exclusiveArea,
				dealDate = dto.getDealDate(),
				dealAmount = dto.dealAmount,
				floor = dto.floor,
				dealCanceled = dto.dealCanceled,
				dealCanceledDate = dto.dealCanceledDate,
			)
		}
	}
}
