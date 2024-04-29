package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.repository.PhotoRepository
import org.gdsc.yonsei.eagleflim.api.repository.PhotoRequestRepository
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
	private val photoRepository: PhotoRepository
) {
	fun createRequest(userInfo: UserInfo, imageList: List<String>, imageProcessType: ImageProcessType) {
		val searchPhotoList = photoRepository.findByPhotoIdListAndStatus(userInfo.userId, imageList, imageStatus = ImageStatus.UPLOADED)
		if (searchPhotoList.size != imageList.size) {
			throw ErrorCd.INVALID_PARAMETER.serviceException("contain invalid image")
		}

		photoRequestRepository.create(PhotoRequest(userId = userInfo.userId, processType = imageProcessType, photoList = imageList))
		// TODO: Request Queue에 삽입 - 추후 논의
	}

	// TODO: 사용자가 요청하는 것은 불가능함 - 서버 요청에 의해서만 접근할 수 있도록 해야 함
	fun updateRequest(requestId: String, requestStatus: RequestStatus) {
		TODO("Not yet implemented")
	}

	fun getSingleRequest(userInfo: UserInfo, requestId: String?): PhotoRequestInfo {
		val request =
			if (requestId != null) photoRequestRepository.findOneByUserIdAndRequestId(userInfo.userId, requestId)
			else photoRequestRepository.findRecentByUserId(userInfo.userId)

		return request?.let {
			PhotoRequestInfoFactory.ofEntry(it)
		} ?: throw ErrorCd.INVALID_PARAMETER.serviceException("not exist request")
	}

	fun getRequestList(userInfo: UserInfo): List<PhotoRequestInfo> {
		return photoRequestRepository.findAllByUserId(userInfo.userId)
			.map { PhotoRequestInfoFactory.ofEntry(it) }
	}
}
