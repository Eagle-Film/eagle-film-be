package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.repository.NotiRepository
import org.gdsc.yonsei.eagleflim.common.entity.Noti
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.springframework.stereotype.Service

@Service
class NotiService(private val notiRepository: NotiRepository) {
	fun createNoti(userInfo: UserInfo, requestId: String, notiToken: String) {
		if (notiRepository.checkExistNoti(userInfo.userId, requestId)) {
			throw ErrorCd.INTERNAL_SERVER_ERROR.serviceException("")
		}

		val noti = Noti(userId = userInfo.userId, requestId = requestId, notiToken = notiToken)
		notiRepository.createNoti(noti)
	}
}
