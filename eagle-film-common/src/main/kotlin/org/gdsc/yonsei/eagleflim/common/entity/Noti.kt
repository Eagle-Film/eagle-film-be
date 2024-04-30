package org.gdsc.yonsei.eagleflim.common.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "noti")
data class Noti(
	@Id val notiId: String = ObjectId.get().toHexString(),
	val userId: String,
	val requestId: String,
	val notiToken: String
)
