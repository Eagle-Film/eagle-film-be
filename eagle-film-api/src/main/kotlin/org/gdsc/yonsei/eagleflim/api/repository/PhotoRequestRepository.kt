package org.gdsc.yonsei.eagleflim.api.repository

import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

@Component
class PhotoRequestRepository(private val mongoTemplate: MongoTemplate) {
	fun create(photoRequest: PhotoRequest) {
		mongoTemplate.insert(photoRequest)
	}

	fun findAllByUserId(userId: String): List<PhotoRequest> {
		val criteria = Criteria.where("userId").`is`(userId)
		return mongoTemplate.find(Query.query(criteria), PhotoRequest::class.java)
	}

	fun findByUserIdWithSize(userId: String, size: Int, lastRequestId: String?): List<PhotoRequest> {
		var criteria = Criteria.where("userId").`is`(userId)
		val sortCriteria = Sort.by("_id")

		if (lastRequestId != null) {
			criteria = criteria.and("lastRequestId").gte(lastRequestId)
		}

		return mongoTemplate.find(Query.query(criteria).with(sortCriteria).limit(size), PhotoRequest::class.java)
	}

	fun findRecentByUserId(userId: String): PhotoRequest? {
		val criteria = Criteria.where("userId").`is`(userId)
		val query = Query.query(criteria)
			.with(Sort.by("createYmdt").descending())

		return mongoTemplate.findOne(query, PhotoRequest::class.java)
	}

	fun findOneByUserIdAndRequestId(userId: String, requestId: String): PhotoRequest? {
		val criteria = Criteria.where("userId").`is`(userId)
			.and("_id").`is`(requestId)

		return mongoTemplate.findOne(Query.query(criteria), PhotoRequest::class.java)
	}

	fun countByUserId(userId: String): Long {
		val criteria = Criteria.where("userId").`is`(userId)
		return mongoTemplate.count(Query.query(criteria), Long::class.java)
	}

	fun updateStatus(photoRequestId: String, requestStatus: RequestStatus) {
		val criteria = Criteria.where("_id").`is`(photoRequestId)
		val update = Update.update("requestStatus", requestStatus)

		mongoTemplate.findAndModify(Query.query(criteria), update, PhotoRequest::class.java)
	}
}
