package org.gdsc.yonsei.eagleflim.common.model

import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import java.time.LocalDateTime

data class PhotoRequestInfo(
	val requestId: String,
	val userId: String,
	val requestStatus: RequestStatus,
	val createYmdt: LocalDateTime,
	val updateYmdt: LocalDateTime
)
