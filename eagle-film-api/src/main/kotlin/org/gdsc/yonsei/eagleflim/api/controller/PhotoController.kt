package org.gdsc.yonsei.eagleflim.api.controller

import org.gdsc.yonsei.eagleflim.api.auth.annotation.EagleUser
import org.gdsc.yonsei.eagleflim.api.controller.model.PhotoCreateRequestResponse
import org.gdsc.yonsei.eagleflim.api.service.PhotoService
import org.gdsc.yonsei.eagleflim.common.model.PhotoInfo
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/photo")
class PhotoController(private val photoService: PhotoService) {
	@PostMapping("/request")
	fun createUploadRequest(@EagleUser userInfo: UserInfo): PhotoCreateRequestResponse {
		logger.debug("[PhotoController] request - userInfo: {}", userInfo)
		val photo = photoService.createPhoto(userInfo)
		return PhotoCreateRequestResponse(photo.photoId)
	}

	@PostMapping("/{imageId}")
	@ResponseStatus(HttpStatus.CREATED)
	fun uploadImage(@EagleUser userInfo: UserInfo, @PathVariable imageId: String, @RequestParam("file") file: MultipartFile) {
		logger.debug("[PhotoController] upload - imageId: {}", imageId)
		photoService.uploadPhoto(userInfo, imageId, file)
	}

	@GetMapping("/{imageId}")
	fun getImage(@EagleUser userInfo: UserInfo, @PathVariable("imageId") imageId: String): PhotoInfo {
		logger.debug("[PhotoController] image - imageId: {}", imageId)
		return photoService.get(userInfo, imageId)
	}

	companion object {
		val logger: Logger = LoggerFactory.getLogger(PhotoController::class.java)
	}
}
