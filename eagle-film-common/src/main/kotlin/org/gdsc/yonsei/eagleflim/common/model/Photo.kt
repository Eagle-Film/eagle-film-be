package org.gdsc.yonsei.eagleflim.common.model

import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import java.time.LocalDateTime

data class Photo(
	val imageId: Long,
	val requestId: Long,
	val userId: Long,
	val imageUrl: String,
	val imageStatus: ImageStatus,
	val createYmdt: LocalDateTime
)
