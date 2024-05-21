package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.repository.NotiRepository
import org.gdsc.yonsei.eagleflim.common.entity.Noti
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.springframework.stereotype.Service

@Service
class NotiService(private val notiRepository: NotiRepository) {
	fun createNoti(userInfo: UserInfo, notiToken: String) {
		val noti = Noti(userId = userInfo.userId, notiToken = notiToken)
		notiRepository.createNoti(noti)
	}
}
