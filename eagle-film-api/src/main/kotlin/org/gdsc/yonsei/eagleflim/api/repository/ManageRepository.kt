package org.gdsc.yonsei.eagleflim.api.repository

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
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
}
