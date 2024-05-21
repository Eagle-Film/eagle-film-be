package org.gdsc.yonsei.eagleflim.api.controller

import org.gdsc.yonsei.eagleflim.api.auth.annotation.EagleUser
import org.gdsc.yonsei.eagleflim.api.controller.model.SetRequestStatusInput
import org.gdsc.yonsei.eagleflim.api.service.ManageService
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*

@Profile("local", "dev")
@RestController
@RequestMapping("/api/v1/manage")
class ManageController(
	private val manageService: ManageService
) {
	@DeleteMapping("/user")
	fun withdraw(@EagleUser userInfo: UserInfo) {
		manageService.withdraw(userInfo)
	}

	@PostMapping("/request")
	fun setRequestStatus(@EagleUser userInfo: UserInfo, @RequestBody setRequestStatusInput: SetRequestStatusInput) {
		manageService.setRequestStatus(userInfo, setRequestStatusInput.requestId, setRequestStatusInput.requestStatus)
	}

	@GetMapping("/noti")
	fun sendNoti(@EagleUser userInfo: UserInfo) {
		manageService.sendNoti(userInfo.userId)
	}
}
