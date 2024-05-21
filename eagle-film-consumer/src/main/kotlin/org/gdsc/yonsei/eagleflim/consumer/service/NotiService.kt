package org.gdsc.yonsei.eagleflim.consumer.service

import org.gdsc.yonsei.eagleflim.consumer.invoker.noti.NotiInvoker
import org.gdsc.yonsei.eagleflim.consumer.repository.NotiRepository
import org.springframework.stereotype.Service

@Service
class NotiService(
	val notiRepository: NotiRepository,
	val notiInvoker: NotiInvoker
) {
	fun sendNoti(userId: String) {
		val notiList = notiRepository.getNotiList(userId)
		notiList.forEach {
			notiInvoker.sendNoti(it.notiToken)
		}
	}
}
