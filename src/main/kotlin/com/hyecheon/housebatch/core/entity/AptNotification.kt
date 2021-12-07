package com.hyecheon.housebatch.core.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/07
 */
@EntityListeners(AuditingEntityListener::class)
@Entity
data class AptNotification(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val aptNotificationId: Long? = null,

	@Column(nullable = false)
	var email: String,

	@Column(nullable = false)
	var guLawdCd: String,

	var enabled: Boolean,

	@CreatedDate
	@Column(nullable = false, updatable = false)
	val createdAt: LocalDateTime = LocalDateTime.now(),

	@LastModifiedDate
	@Column(nullable = false)
	val updatedAt: LocalDateTime = LocalDateTime.now(),
)