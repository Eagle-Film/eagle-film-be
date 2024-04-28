package org.gdsc.yonsei.eagleflim.common.entity

import org.bson.types.ObjectId
import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "user")
data class User(
	@Id val userId: ObjectId,
	val userName: String,
	val requestStatus: RequestStatus,
	val oauthProvider: OAuthProvider,
	val oauthIdentifier: String,
	val createYmdt: LocalDateTime = LocalDateTime.now(),
	val requestYmdt: LocalDateTime
)
