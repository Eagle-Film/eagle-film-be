package org.gdsc.yonsei.eagleflim.api.exception

import org.springframework.http.HttpStatus

enum class ErrorCd(val httpStatus: HttpStatus, val message: String) {
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "invalid parameter"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "authentication error"),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal error"),

	NOT_EXIST_IMAGE(HttpStatus.NOT_FOUND, "not found image"),
	TOTAL_MAX_IMAGE_EXCEED(HttpStatus.TOO_MANY_REQUESTS, "total max image exceed");

	fun serviceException(message: String): ServiceException {
		return ServiceException(this)
	}
}

