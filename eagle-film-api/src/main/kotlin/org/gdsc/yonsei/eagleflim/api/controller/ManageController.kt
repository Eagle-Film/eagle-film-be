package org.gdsc.yonsei.eagleflim.api.controller

import org.gdsc.yonsei.eagleflim.api.auth.annotation.EagleUser
import org.gdsc.yonsei.eagleflim.api.service.ManageService
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
