package org.gdsc.yonsei.eagleflim.common.entity

import org.bson.types.ObjectId
import org.gdsc.yonsei.eagleflim.common.model.type.ImageProcessType
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "photoRequest")
data class PhotoRequest(
	@Id val requestId: String = ObjectId.get().toHexString(),
	val userId: String,
	val requestStatus: RequestStatus = RequestStatus.WAITING,
	val processType: ImageProcessType,
	val photoList: List<String>,
	val resultPhoto: String? = null,
	val createYmdt: LocalDateTime = LocalDateTime.now(),
	val updateYmdt: LocalDateTime = LocalDateTime.now(),
)
