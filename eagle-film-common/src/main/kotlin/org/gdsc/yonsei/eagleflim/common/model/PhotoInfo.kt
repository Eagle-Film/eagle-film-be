package org.gdsc.yonsei.eagleflim.common.model

import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import java.time.LocalDateTime

data class PhotoInfo(
	val imageId: String,
	val requestId: String?,
	val userId: String,
	val imageUrl: String?,
	val imageStatus: ImageStatus,
	val createYmdt: LocalDateTime
)
