package org.gdsc.yonsei.eagleflim.api.controller

import org.gdsc.yonsei.eagleflim.api.auth.annotation.EagleUser
import org.gdsc.yonsei.eagleflim.api.controller.model.NotiRequestInput
import org.gdsc.yonsei.eagleflim.api.service.NotiService
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/noti")
class NotiController(val notiService: NotiService) {
	@PostMapping
	fun createNoti(@EagleUser userInfo: UserInfo, @RequestBody notiRequestInput: NotiRequestInput) {
		notiService.createNoti(userInfo, notiRequestInput.tokenId)
	}
}
