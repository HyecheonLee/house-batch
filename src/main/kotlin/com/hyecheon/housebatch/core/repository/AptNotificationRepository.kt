package com.hyecheon.housebatch.core.repository

import com.hyecheon.housebatch.core.entity.AptNotification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository


/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/07
 */
interface AptNotificationRepository : JpaRepository<AptNotification, Long> {
	fun findByEnabledIsTrue(pageable: Pageable): Page<AptNotification>
}