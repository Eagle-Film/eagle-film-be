package org.gdsc.yonsei.eagleflim.api.config.mvc

import jakarta.servlet.http.HttpServletRequest
import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.exception.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class ExceptionControllerAdvice {
	@ExceptionHandler(ServiceException::class)
	fun serviceException(request: HttpServletRequest?, exception: ServiceException): ResponseEntity<ErrorCd> {
		val errorCd: ErrorCd = exception.errorCode
		return ResponseEntity.status(errorCd.httpStatus.value()).body(errorCd)
	}

	@ExceptionHandler(NoResourceFoundException::class)
	fun notFoundException(): ResponseEntity<ErrorCd> {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCd.NOT_FOUND)
	}
}
