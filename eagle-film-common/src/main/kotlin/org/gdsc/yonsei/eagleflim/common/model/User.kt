package org.gdsc.yonsei.eagleflim.common.model

import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import java.time.LocalDateTime

data class User(
	val userId: String,
	val userName: String,
	val requestStatus: RequestStatus,
	val createYmdt: LocalDateTime,
	val requestYmdt: LocalDateTime
)
