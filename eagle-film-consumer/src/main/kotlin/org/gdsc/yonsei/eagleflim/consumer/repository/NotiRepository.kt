package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.common.entity.Noti
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class NotiRepository(
	val mongoTemplate: MongoTemplate
) {
	fun getNotiList(userId: String): List<Noti> {
		val criteria = Criteria.where("userId").`is`(userId)
		return mongoTemplate.find(Query.query(criteria), Noti::class.java)
	}
}
