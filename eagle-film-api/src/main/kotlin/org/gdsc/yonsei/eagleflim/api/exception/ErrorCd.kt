package org.gdsc.yonsei.eagleflim.api.exception

import org.springframework.http.HttpStatus

enum class ErrorCd(val httpStatus: HttpStatus, val message: String) {
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad request"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized"),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "acl failed"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "not found"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "method not allowed"),
	NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "not acceptable"),
	CONFLICT(HttpStatus.CONFLICT, "conflict"),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "unsupported media type"),
	TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "too many requests"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "service unavailable"),
	NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "no handler found"),

	NOT_SUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "not supported provider"),
	INVALID_CODE(HttpStatus.BAD_REQUEST, "invalid code"),
	INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "invalid refresh token"),
	REQUEST_ALREADY_EXISTS(HttpStatus.CONFLICT, "request already exists"),
	REQUEST_LIMIT_EXCEED(HttpStatus.TOO_MANY_REQUESTS, "request limit exceeded"),
	NOT_EXIST_REQUEST(HttpStatus.NOT_FOUND, "not exist request"),

	IMAGE_LIMIT_EXCEED(HttpStatus.BAD_REQUEST, "image limit exceeded"),
	ALREADY_UPLOADED(HttpStatus.BAD_REQUEST, "already uploaded"),
	NOT_UPLOADED_IMAGE(HttpStatus.BAD_REQUEST, "contained not uploaded image"),

	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "invalid parameter"),
	NOT_EXIST_IMAGE(HttpStatus.NOT_FOUND, "not found image"),
	TOTAL_MAX_IMAGE_EXCEED(HttpStatus.TOO_MANY_REQUESTS, "total max image exceed");

	fun serviceException(message: String? = null): ServiceException {
		return ServiceException(this, message = message ?: this.message)
	}
}

