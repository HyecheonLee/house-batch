package com.hyecheon.housebatch.core.entity

import com.hyecheon.housebatch.core.converter.LawdConverter
import org.mapstruct.factory.Mappers
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

	@Column(nullable = false, unique = true)
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
) {
	fun update(source: Lawd) = run {
		val converter = Mappers.getMapper(LawdConverter::class.java)
		converter.update(source, this)
	}
}