package org.gdsc.yonsei.eagleflim.common.model.factory

import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.model.PhotoRequestInfo

object PhotoRequestInfoFactory {
	fun ofEntry(photoRequest: PhotoRequest, originalImageList: List<Photo>, resultImage: Photo?): PhotoRequestInfo {
		return PhotoRequestInfo(photoRequest.requestId, photoRequest.userId, photoRequest.requestStatus, photoRequest.createYmdt, photoRequest.updateYmdt,
			originalImageList.map(PhotoInfoFactory::ofEntry), resultImage?.let(PhotoInfoFactory::ofEntry))
	}
}
