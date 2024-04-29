package org.gdsc.yonsei.eagleflim.api.repository

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class UserRepository(
	val mongoTemplate: MongoTemplate
) {
	fun createUser(user: User): User {
		return mongoTemplate.insert(user)
	}

	fun getUserByOAuthInfo(oauthProvider: OAuthProvider, oauthIdentifier: String): User? {
		val criteria = Criteria.where("oauthProvider").`is`(oauthProvider)
			.and("oauthIdentifier").`is`(oauthIdentifier)

		return mongoTemplate.findOne(Query.query(criteria), User::class.java)
	}

	fun getUser(id: String): User? {
		val criteria = Criteria.where("_id").`is`(id)
		return mongoTemplate.findOne(Query.query(criteria), User::class.java)
	}
}
