package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.consumer.invoker.NodeInvoker
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RequestRepository(
	private val mongoTemplate: MongoTemplate,
	private val stringRedisTemplate: RedisTemplate<String, String>,
	private val nodeInvoker: NodeInvoker
) {
	fun selectAllWaitingRequests(): Set<String> {
		return stringRedisTemplate.opsForSet().members(REDIS_LABEL_REQUEST) ?: setOf()
	}

	companion object {
		const val REDIS_LABEL_REQUEST: String = "Request"
	}
}
