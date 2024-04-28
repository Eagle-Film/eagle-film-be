package org.gdsc.yonsei.eagleflim.common.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "photo")
data class Photo(
	@Id val photoId: ObjectId,
	val userId: ObjectId,
	val photoLocation: String,
	val createYmdt: LocalDateTime = LocalDateTime.now(),
)
