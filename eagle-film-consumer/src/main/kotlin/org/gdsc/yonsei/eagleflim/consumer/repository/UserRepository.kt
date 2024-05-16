package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

@Component
class UserRepository(
	val mongoTemplate: MongoTemplate
) {
	fun updateRequestStatus(id: String, requestStatus: RequestStatus) {
		val criteria = Criteria.where("_id").`is`(id)
		val update = Update.update("requestStatus", requestStatus)
		mongoTemplate.findAndModify(Query.query(criteria), update, User::class.java)
	}
}
