package org.gdsc.yonsei.eagleflim.api.controller

import org.gdsc.yonsei.eagleflim.api.auth.annotation.EagleUser
import org.gdsc.yonsei.eagleflim.api.controller.model.PhotoRequestCreateInput
import org.gdsc.yonsei.eagleflim.api.controller.model.PhotoRequestStatusResponse
import org.gdsc.yonsei.eagleflim.api.service.PhotoRequestService
import org.gdsc.yonsei.eagleflim.common.model.PhotoRequestInfo
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/photo-request")
class PhotoRequestController(private val photoRequestService: PhotoRequestService) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@EagleUser userInfo: UserInfo, @RequestBody photoRequestCreateInput: PhotoRequestCreateInput) {
		photoRequestService.createRequest(userInfo, photoRequestCreateInput.imageList, photoRequestCreateInput.imageProcessType)
	}

	@GetMapping
	fun getList(@EagleUser userInfo: UserInfo): List<PhotoRequestInfo> {
		return photoRequestService.getRequestList(userInfo)
	}

	@GetMapping("/{requestId}")
	fun get(@EagleUser userInfo: UserInfo, @PathVariable(required = false) requestId: String?): PhotoRequestInfo {
		return photoRequestService.getSingleRequest(userInfo, requestId)
	}

	@GetMapping("/check/{requestId}")
	fun check(@EagleUser userInfo: UserInfo, @PathVariable(required = false) requestId: String?): PhotoRequestStatusResponse {
		val result = photoRequestService.getSingleRequest(userInfo, requestId)
		return PhotoRequestStatusResponse(result.requestStatus)
	}
}