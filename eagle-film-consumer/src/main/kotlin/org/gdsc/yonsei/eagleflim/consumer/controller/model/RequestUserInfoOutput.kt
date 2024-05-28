package org.gdsc.yonsei.eagleflim.consumer.controller.model

import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.entity.User

data class RequestUserInfoOutput(
	val requestInfo: PhotoRequest?,
	val userInfo: User?
)
