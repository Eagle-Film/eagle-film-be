package org.gdsc.yonsei.eagleflim.consumer.service

import org.gdsc.yonsei.eagleflim.consumer.repository.NodeRepository
import org.gdsc.yonsei.eagleflim.consumer.repository.RequestRepository
import org.springframework.stereotype.Service

@Service
class PhotoRequestService(
	private val requestRepository: RequestRepository,
	private val nodeRepository: NodeRepository
) {
	fun assignRequest(requestId: String, nodeUrl: String) {
		// 데이터 존재여부 확인
		// 이미지 서버에서 전부 다운로드
		// base64로 변환
		// node에 요청
	}

	fun completeRequest(requestId: String, resultImage: String) {
		// 이미지 변환
		// 서버에 저장
		// db에 데이터 갱신 작업 진행
		// redis 상태 갱신
	}
}
