package org.gdsc.yonsei.eagleflim.common.model

import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import java.time.LocalDateTime

data class PhotoRequest(
	val requestId: Long,
	val userId: Long,
	val requestStatus: RequestStatus,
	val createYmdt: LocalDateTime,
	val updateYmdt: LocalDateTime
)
