package org.gdsc.yonsei.eagleflim.common.model

import org.gdsc.yonsei.eagleflim.common.model.type.ImageProcessType
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import java.time.LocalDateTime

data class SimplePhotoRequestInfo(
	val request: String,
	val imageProcessType: ImageProcessType,
	val requestStatus: RequestStatus,
	val createYmdt: LocalDateTime,
	val updateYmdt: LocalDateTime
) {
}
