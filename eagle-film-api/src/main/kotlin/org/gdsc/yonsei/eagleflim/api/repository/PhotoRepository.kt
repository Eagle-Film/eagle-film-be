package org.gdsc.yonsei.eagleflim.api.repository

import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

@Component
class PhotoRepository(val mongoTemplate: MongoTemplate) {
	fun imageCount(userId: String): Int {
		val criteria = Criteria.where("userId").`is`(userId)
		return mongoTemplate.count(Query.query(criteria), Photo::class.java).toInt()
	}

	fun getPhoto(userId: String, photoId: String): Photo? {
		val criteria = Criteria.where("userId").`is`(userId).and("_id").`is`(photoId)
		return mongoTemplate.findOne(Query.query(criteria), Photo::class.java)
	}

	fun findByPhotoIdListAndStatus(userId: String, photoIdList: List<String>, imageStatus: ImageStatus = ImageStatus.PROCESSED): List<Photo> {
		val criteria = Criteria.where("userId").`is`(userId)
			.and("imageStatus").`is`(imageStatus)
			.and("_id").`in`(photoIdList)
		return mongoTemplate.find(query(criteria), Photo::class.java)
	}

	fun updatePhoto(userId: String, photoId: String, imageStatus: ImageStatus, imageLocation: String) {
		val criteria = Criteria.where("userId").`is`(userId).and("_id").`is`(photoId)
		val update = Update.update("imageStatus", imageStatus)
			.set("imageUrl", imageLocation)

		mongoTemplate.findAndModify(Query.query(criteria), update, Photo::class.java)
	}

	fun insertPhoto(photo: Photo): Photo {
		return mongoTemplate.insert(photo)
	}
}
