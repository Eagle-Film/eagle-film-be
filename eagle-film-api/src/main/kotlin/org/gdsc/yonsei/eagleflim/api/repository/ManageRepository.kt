package org.gdsc.yonsei.eagleflim.api.repository

import org.gdsc.yonsei.eagleflim.common.entity.Noti
import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

/**
 * 개발용 Repository 이므로, 실제 로직과 별개로 한곳에 몰아넣음
 */
@Component
class ManageRepository(
	private val mongoTemplate: MongoTemplate
) {
	fun withdraw(userId: String) {
		val criteria = Criteria.where("id").`is`(userId)
		mongoTemplate.remove(Query.query(criteria), User::class.java)
	}

	fun setRequestStatus(requestId: String, requestStatus: RequestStatus) {
		val criteria = Criteria.where("id").`is`(requestId)
		val update = Update.update("requestStatus", requestStatus)

		mongoTemplate.findAndModify(Query.query(criteria), update, PhotoRequest::class.java)
	}

	fun getNotiList(userId: String): List<Noti> {
		val criteria = Criteria.where("userId").`is`(userId)
		return mongoTemplate.find(Query.query(criteria), Noti::class.java)
	}
}
