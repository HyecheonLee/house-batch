package com.hyecheon.housebatch.core.repository

import com.hyecheon.housebatch.core.entity.Lawd
import org.springframework.data.jpa.repository.JpaRepository

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
interface LawdRepository : JpaRepository<Lawd, Long>