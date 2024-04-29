package org.gdsc.yonsei.eagleflim.common.model.factory

import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.gdsc.yonsei.eagleflim.common.model.PhotoInfo

object PhotoInfoFactory {
	fun ofEntry(photo: Photo): PhotoInfo {
		return PhotoInfo(photo.photoId, null, photo.userId, photo.photoLocation, photo.imageStatus, photo.createYmdt)
	}
}
