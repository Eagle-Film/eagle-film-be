package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.gdsc.yonsei.eagleflim.consumer.invoker.NodeInvoker
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class RequestRepository(
	private val mongoTemplate: MongoTemplate,
	private val stringRedisTemplate: RedisTemplate<String, String>,
	private val nodeInvoker: NodeInvoker
) {
	fun selectAllWaitingRequests(): Set<String> {
		return stringRedisTemplate.opsForSet().members(REDIS_LABEL_REQUEST) ?: setOf()
	}

	fun selectOneRequest(requestId: String): PhotoRequest? {
		return mongoTemplate.findOne(Query.query(Criteria.where("_id").`is`(requestId)), PhotoRequest::class.java)
	}

	fun updateStatus(requestId: String, status: RequestStatus) {
		val criteria = Criteria.where("_id").`is`(requestId)
		val update = Update.update("status", status)
			.set("updateYmdt", LocalDateTime.now())

		mongoTemplate.findAndModify(Query.query(criteria), update, PhotoRequest::class.java)
	}

	companion object {
		const val REDIS_LABEL_REQUEST: String = "Request"
	}
}