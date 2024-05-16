package org.gdsc.yonsei.eagleflim.consumer.service

import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.gdsc.yonsei.eagleflim.consumer.infra.BucketFileHelper
import org.gdsc.yonsei.eagleflim.consumer.invoker.NodeInvoker
import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
import org.gdsc.yonsei.eagleflim.consumer.repository.NodeRepository
import org.gdsc.yonsei.eagleflim.consumer.repository.PhotoRepository
import org.gdsc.yonsei.eagleflim.consumer.repository.RequestRepository
import org.gdsc.yonsei.eagleflim.consumer.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PhotoRequestService(
	private val photoRepository: PhotoRepository,
	private val requestRepository: RequestRepository,
	private val nodeRepository: NodeRepository,
	private val userRepository: UserRepository,
	private val nodeInvoker: NodeInvoker,
	private val bucketFileHelper: BucketFileHelper
) {
	fun assignRequest(requestId: String, nodeUrl: String) {
		val request = requestRepository.selectOneRequest(requestId) ?: error("Request with requestId $requestId not found")
		// TODO: 데이터 정합성 체크 - DB 조회 + 본인이 만든 이미지인지 체크할 필요가 있음
		val imageList = request.photoList.map {
			bucketFileHelper.downloadFile(it)
		}

		nodeInvoker.requestInference(nodeUrl, imageList, request.processType)
		nodeRepository.updateNodeInfo(NodeInfo(nodeUrl, waiting = false, assignedRequest = requestId))
		requestRepository.updateStatus(requestId, RequestStatus.PROCESSING)
		requestRepository.deleteRequest(requestId)
		userRepository.updateRequestStatus(request.userId, RequestStatus.PROCESSING)
	}

	fun completeRequest(nodeUrl: String, requestId: String, resultImage: String) {
		val request = requestRepository.selectOneRequest(requestId) ?: error("Request with requestId $requestId not found")
		val photo = Photo(userId = request.userId, imageStatus = ImageStatus.PROCESSED)

		val uploadedUrl = bucketFileHelper.uploadFile(photo.photoId, bucketFileHelper.convertToFile(resultImage))
		val processedPhoto = photo.copy(imageUrl = uploadedUrl)

		photoRepository.insertPhoto(processedPhoto)
		requestRepository.updateStatusWithImage(requestId, RequestStatus.COMPLETED, processedPhoto.photoId)
		userRepository.updateRequestStatus(request.userId, RequestStatus.COMPLETED)
		nodeRepository.updateNodeInfo(NodeInfo(nodeUrl, waiting = true, assignedRequest = null))

		// TODO: Web Push Noti
	}
}
