package org.gdsc.yonsei.eagleflim.common.entity

import org.bson.types.ObjectId
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "PhotoRequest")
data class PhotoRequest(
	@Id val requestId: ObjectId,
	val userId: ObjectId,
	val requestStatus: RequestStatus,
	val photoList: List<ObjectId>,
	val createYmdt: LocalDateTime = LocalDateTime.now(),
	val updateYmdt: LocalDateTime = LocalDateTime.now()
)
