package org.gdsc.yonsei.eagleflim.common.model.factory

import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.model.PhotoRequestInfo
import org.gdsc.yonsei.eagleflim.common.model.SimplePhotoRequestInfo

object PhotoRequestInfoFactory {
	fun ofEntry(photoRequest: PhotoRequest, originalImageList: List<Photo>, resultImage: Photo?): PhotoRequestInfo {
		return PhotoRequestInfo(photoRequest.requestId, photoRequest.userId, photoRequest.processType, photoRequest.requestStatus, photoRequest.createYmdt, photoRequest.updateYmdt,
			originalImageList.map(PhotoInfoFactory::ofEntry), resultImage?.let(PhotoInfoFactory::ofEntry))
	}

	fun simpleOfEntry(photoRequest: PhotoRequest): SimplePhotoRequestInfo {
		return SimplePhotoRequestInfo(photoRequest.requestId, photoRequest.processType, photoRequest.requestStatus, photoRequest.createYmdt, photoRequest.updateYmdt)
	}
}
