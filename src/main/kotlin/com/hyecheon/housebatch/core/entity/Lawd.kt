package com.hyecheon.housebatch.core.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
@Entity
data class Lawd(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val lawdId: Long? = null,

	@Column(nullable = false)
	var lawdCd: String? = null,

	@Column(nullable = false)
	var lawdDong: String? = null,

	@Column(nullable = false)
	var exist: Boolean? = null,

	@CreatedDate
	@Column(updatable = false)
	val createdAt: LocalDateTime = LocalDateTime.now(),

	@LastModifiedDate
	val updatedAt: LocalDateTime = LocalDateTime.now()
)