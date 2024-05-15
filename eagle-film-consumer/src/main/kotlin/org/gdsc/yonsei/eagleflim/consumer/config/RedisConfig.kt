package org.gdsc.yonsei.eagleflim.consumer.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
open class RedisConfig(
	@Value("\${spring.data.redis.host}") private val host: String,
	@Value("\${spring.data.redis.port}") private val port: Int
) {
	@Bean
	open fun redisConnectionFactory(): RedisConnectionFactory {
		return LettuceConnectionFactory(host, port)
	}
}
