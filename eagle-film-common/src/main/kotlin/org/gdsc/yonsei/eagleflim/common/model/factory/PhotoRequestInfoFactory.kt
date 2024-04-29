package org.gdsc.yonsei.eagleflim.common.model.factory

import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.model.PhotoRequestInfo

object PhotoRequestInfoFactory {
	fun ofEntry(photoRequest: PhotoRequest): PhotoRequestInfo {
		return PhotoRequestInfo(photoRequest.requestId, photoRequest.userId, photoRequest.requestStatus, photoRequest.createYmdt, photoRequest.updateYmdt)
	}
}
