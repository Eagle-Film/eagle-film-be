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

	fun removeUser(id: String): Long {
		val criteria = Criteria.where("_id").`is`(id)
		return mongoTemplate.remove(Query.query(criteria), User::class.java).deletedCount
	}

	fun findByUserName(userName: String): List<User> {
		val criteria = Criteria.where("userName").`is`(userName)
		return mongoTemplate.find(Query.query(criteria), User::class.java) ?: listOf()
	}

	fun findById(userId: String): User? {
		val criteria = Criteria.where("userId").`is`(userId)
		return mongoTemplate.findOne(Query.query(criteria), User::class.java)
	}
}
