package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.repository.PhotoRepository
import org.gdsc.yonsei.eagleflim.api.repository.PhotoRequestRepository
import org.gdsc.yonsei.eagleflim.api.repository.UserRepository
import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.model.PhotoRequestInfo
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.gdsc.yonsei.eagleflim.common.model.factory.PhotoRequestInfoFactory
import org.gdsc.yonsei.eagleflim.common.model.type.ImageProcessType
import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.stereotype.Service

@Service
class PhotoRequestService(
	private val photoRequestRepository: PhotoRequestRepository,
	private val photoRepository: PhotoRepository,
	private val userRepository: UserRepository
) {
	/**
	 * 이미지 처리 요청을 생성한다.
	 */
	fun createRequest(userInfo: UserInfo, imageList: List<String>, imageProcessType: ImageProcessType) {
		validateRequest(userInfo.userId)
		validateImage(userInfo.userId, imageList)

		val photoRequest = PhotoRequest(userId = userInfo.userId, processType = imageProcessType, photoList = imageList)
		photoRequestRepository.create(photoRequest)
		photoRequestRepository.submitRequest(photoRequest.requestId)
		userRepository.updateRequestStatus(userInfo.userId, RequestStatus.WAITING)
	}

	// TODO: 사용자가 요청하는 것은 불가능함 - 서버 요청에 의해서만 접근할 수 있도록 해야 함
	fun updateRequest(requestId: String, requestStatus: RequestStatus) {
		TODO("Not yet implemented")
	}

	fun getSingleRequest(userInfo: UserInfo, requestId: String?): PhotoRequestInfo {
		val request =
			if (requestId != null) photoRequestRepository.findOneByUserIdAndRequestId(userInfo.userId, requestId)
			else photoRequestRepository.findRecentByUserId(userInfo.userId)

		return request?.let(this::createPhotoRequestInfo) ?: throw ErrorCd.NOT_EXIST_REQUEST.serviceException("not exist request")
	}

	fun getRequestList(userInfo: UserInfo, size: Int, pageToken: String?): List<PhotoRequestInfo> {
		return photoRequestRepository.findByUserIdWithSize(userInfo.userId, size, pageToken)
			.map(this::createPhotoRequestInfo)
	}

	private fun createPhotoRequestInfo(photoRequest: PhotoRequest): PhotoRequestInfo {
		val userId = photoRequest.userId
		val originalImageList = photoRepository.findByPhotoIdList(userId, photoRequest.photoList)
		val resultImage = photoRequest.resultImage?.let { photoRepository.getPhoto(userId, it) }
		return PhotoRequestInfoFactory.ofEntry(photoRequest, originalImageList, resultImage)
	}

	private fun validateRequest(userId: String) {
		val photoRequestList = photoRequestRepository.findAllByUserId(userId)
		if (photoRequestList.size >= REQUEST_LIMIT) {
			throw ErrorCd.REQUEST_LIMIT_EXCEED.serviceException()
		}

		photoRequestList.forEach {
			if (it.requestStatus == RequestStatus.PROCESSING) {
				throw ErrorCd.REQUEST_ALREADY_EXISTS.serviceException()
			}
		}
	}

	private fun validateImage(userId: String, imageList: List<String>) {
		if (imageList.size > IMAGE_LIMIT) {
			throw ErrorCd.IMAGE_LIMIT_EXCEED.serviceException()
		}

		val searchPhotoList = photoRepository.findByPhotoIdList(userId, imageList)
		if (searchPhotoList.size != imageList.size) {
			throw ErrorCd.NOT_EXIST_IMAGE.serviceException()
		}

		searchPhotoList.forEach {
			if (it.imageStatus != ImageStatus.UPLOADED) {
				throw ErrorCd.NOT_UPLOADED_IMAGE.serviceException()
			}
		}
	}

	companion object {
		const val REQUEST_LIMIT = 500 // TODO: 추후 논의 필요
		const val IMAGE_LIMIT = 100 // TODO: 추후 논의 필요
	}
}
