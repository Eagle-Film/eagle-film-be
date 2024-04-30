package org.gdsc.yonsei.eagleflim.api.repository

import org.gdsc.yonsei.eagleflim.common.entity.Noti
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class NotiRepository(private val mongoTemplate: MongoTemplate) {
	fun createNoti(noti: Noti) {
		mongoTemplate.insert(noti)
	}

	fun checkExistNoti(userId: String, requestId: String): Boolean{
		val criteria = Criteria.where("userId").`is`(userId)
			.and("requestId").`is`(requestId)

		return mongoTemplate.exists(Query.query(criteria), Noti::class.java)
	}
}
