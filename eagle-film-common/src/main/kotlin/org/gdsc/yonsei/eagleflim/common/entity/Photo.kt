package org.gdsc.yonsei.eagleflim.common.entity

import org.bson.types.ObjectId
import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "photo")
data class Photo(
	@Id val photoId: String = ObjectId.get().toHexString(),
	val userId: String,
	val photoLocation: String? = null,
	val imageStatus: ImageStatus = ImageStatus.NOT_UPLOADED,
	val createYmdt: LocalDateTime = LocalDateTime.now(),
)
