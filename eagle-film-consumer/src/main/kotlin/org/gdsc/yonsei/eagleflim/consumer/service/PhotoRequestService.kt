package org.gdsc.yonsei.eagleflim.consumer.service

import org.gdsc.yonsei.eagleflim.common.entity.Photo
import org.gdsc.yonsei.eagleflim.common.model.type.ImageStatus
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.gdsc.yonsei.eagleflim.consumer.infra.BucketFileHelper
import org.gdsc.yonsei.eagleflim.consumer.invoker.NodeInvoker
import org.gdsc.yonsei.eagleflim.consumer.invoker.bg.BgInvoker
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
	private val bgInvoker: BgInvoker,
	private val nodeRepository: NodeRepository,
	private val userRepository: UserRepository,
	private val nodeInvoker: NodeInvoker,
	private val notiService: NotiService,
	private val bucketFileHelper: BucketFileHelper,
	private val nodeService: NodeService
) {
	fun assignRequest(requestId: String, nodeUrl: String) {
		val request = requestRepository.selectOneRequest(requestId) ?: error("Request with requestId $requestId not found")
		// TODO: 데이터 정합성 체크 - DB 조회 + 본인이 만든 이미지인지 체크할 필요가 있음
		val imageList = request.photoList.map {
			bucketFileHelper.downloadFile(it)
		}

		nodeInvoker.requestInference(nodeUrl, requestId, imageList, request.processType)
		nodeRepository.updateNodeInfo(NodeInfo(nodeUrl, waiting = false, assignedRequest = requestId))
		requestRepository.updateStatus(requestId, RequestStatus.PROCESSING)
		requestRepository.deleteRequest(requestId)
		userRepository.updateRequestStatus(request.userId, RequestStatus.PROCESSING)
	}

	fun completeRequest(nodeUrl: String, requestId: String, resultImage: String) {
		// Phase 1. 서버로부터 이미지 응답
		val request = requestRepository.selectOneRequest(requestId) ?: error("Request with requestId $requestId not found")
		val semiProcessedImage = saveImage(request.userId, ImageStatus.SEMI_PROCESSED, resultImage = resultImage)
		nodeRepository.updateNodeInfo(NodeInfo(nodeUrl, waiting = true, assignedRequest = null))

		// Phase 2. BG 호출
		val bgResponse = bgInvoker.sendRequest(semiProcessedImage.imageUrl!!) ?: bucketFileHelper.convertToFile(resultImage)
		val processedImage = saveImage(request.userId, ImageStatus.PROCESSED, resultImageByte =  bgResponse)

		userRepository.updateRequestStatus(request.userId, RequestStatus.COMPLETED)
		requestRepository.updateStatusWithImage(requestId, RequestStatus.COMPLETED, processedImage.photoId)

		// TODO: Web Push Noti
		notiService.sendNoti(request.userId)

		if (nodeRepository.checkDeleteQueue(nodeUrl)) {
			nodeRepository.removeDeleteQueue(nodeUrl)
			nodeService.removeNode(nodeUrl)
		}
	}

	private fun saveImage(userId: String, imageStatus: ImageStatus, resultImage: String? = null, resultImageByte: ByteArray? = null): Photo {
		val photo = Photo(userId = userId, imageStatus = imageStatus)
		val image = resultImageByte ?: bucketFileHelper.convertToFile(resultImage!!)
		val uploadedUrl = bucketFileHelper.uploadFile(photo.photoId, image)
		val processedPhoto = photo.copy(imageUrl = uploadedUrl)

		photoRepository.insertPhoto(processedPhoto)
		return processedPhoto
	}

	fun retryRequest(requestId: String) {
		val request = requestRepository.selectOneRequest(requestId) ?: error("Request not exist")
		if (request.requestStatus == RequestStatus.WAITING || request.requestStatus == RequestStatus.PROCESSING) {
			error("awaiting requests can not retry.")
		}

		requestRepository.updateStatus(requestId, RequestStatus.WAITING)
		requestRepository.createRequest(requestId)
	}
}
