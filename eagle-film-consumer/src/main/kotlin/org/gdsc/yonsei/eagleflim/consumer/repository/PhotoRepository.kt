package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

@Component
class PhotoRepository(
	val mongoTemplate: MongoTemplate
) {
	fun insertPhoto(photo: Photo) {
		mongoTemplate.insert(photo)
	}
}
