package com.hyecheon.housebatch.adapter

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/07
 */
interface SendService {
	fun send(email: String, message: String)
}