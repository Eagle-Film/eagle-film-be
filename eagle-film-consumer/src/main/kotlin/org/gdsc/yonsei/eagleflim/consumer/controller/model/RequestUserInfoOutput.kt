package org.gdsc.yonsei.eagleflim.consumer.controller.model

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.common.model.SimplePhotoRequestInfo

data class RequestUserInfoOutput(
	val requestInfo: SimplePhotoRequestInfo?,
	val userInfo: User?
)
