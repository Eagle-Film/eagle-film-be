package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.invoker.noti.NotiInvoker
import org.gdsc.yonsei.eagleflim.api.repository.ManageRepository
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

/**
 * 개발용 Service 이므로, 실제 로직과 별개로 한곳에 몰아넣음
 */
@Service
@Profile("local", "dev")
class ManageService(
	private val manageRepository: ManageRepository,
	private val notiInvoker: NotiInvoker
) {
	fun withdraw(userInfo: UserInfo) {
		manageRepository.withdraw(userInfo.userId)
	}

	fun setRequestStatus(userInfo: UserInfo, requestId: String, requestStatus: RequestStatus) {
		manageRepository.setRequestStatus(requestId, requestStatus)
	}

	fun sendNoti(userId: String) {
		val notiList = manageRepository.getNotiList(userId)
		notiList.forEach {
			notiInvoker.sendNoti(it.notiToken)
		}
	}
}
