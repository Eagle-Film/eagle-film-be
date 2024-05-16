package org.gdsc.yonsei.eagleflim.common.model

import org.gdsc.yonsei.eagleflim.common.model.type.ImageProcessType
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import java.time.LocalDateTime

data class PhotoRequestInfo(
	val requestId: String,
	val userId: String,
	val imageProcessType: ImageProcessType,
	val requestStatus: RequestStatus,
	val createYmdt: LocalDateTime,
	val updateYmdt: LocalDateTime,
	val originalImages: List<PhotoInfo>,
	val resultImage: PhotoInfo?
)
