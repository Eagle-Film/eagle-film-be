package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.infra.BucketFileHelper
import org.gdsc.yonsei.eagleflim.api.repository.PhotoRepository
import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.gdsc.yonsei.eagleflim.common.model.PhotoInfo
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.gdsc.yonsei.eagleflim.common.model.factory.PhotoInfoFactory
import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PhotoService(
	private val photoRepository: PhotoRepository,
	private val fileHelper: BucketFileHelper
) {
	fun createPhoto(userInfo: UserInfo): Photo {
		val photoCount = photoRepository.imageCount(userInfo.userId)

		if (photoCount >= MAX_PHOTO_LIMIT) {
			throw ErrorCd.TOTAL_MAX_IMAGE_EXCEED.serviceException("total photo limit exceeded")
		}

		val photo = Photo(userId = userInfo.userId)
		return photoRepository.insertPhoto(photo)
	}

	fun uploadPhoto(userInfo: UserInfo, photoId: String, multipartFile: MultipartFile) {
		val photo = photoRepository.getPhoto(userInfo.userId, photoId) ?: throw ErrorCd.NOT_EXIST_IMAGE.serviceException("not exist image")
		if (photo.imageStatus != ImageStatus.NOT_UPLOADED) {
			throw ErrorCd.INVALID_PARAMETER.serviceException("image already uploaded")
		}

		val path = fileHelper.uploadFile(photoId, multipartFile)
		photoRepository.updatePhoto(userInfo.userId, photoId, ImageStatus.UPLOADED, path.toString())
	}

	fun get(userInfo: UserInfo, photoId: String): PhotoInfo {
		val photo = photoRepository.getPhoto(userInfo.userId, photoId) ?: throw ErrorCd.NOT_EXIST_IMAGE.serviceException("not exist image")
		return PhotoInfoFactory.ofEntry(photo)
	}

	companion object {
		const val MAX_PHOTO_LIMIT = 5
	}
}
