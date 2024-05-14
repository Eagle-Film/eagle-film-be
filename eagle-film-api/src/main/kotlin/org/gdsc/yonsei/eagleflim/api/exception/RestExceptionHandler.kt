package org.gdsc.yonsei.eagleflim.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.util.*

@RestControllerAdvice
class RestExceptionHandler {
	private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

	/**
	 * ServiceException
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(ServiceException::class)
	fun serviceException(request: HttpServletRequest, exception: ServiceException): ResponseEntity<Response> {
		val errorCd = exception.errorCode
		return handleException(request, exception, errorCd, exception.message)
	}

	/**
	 * spring - request exception
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(
		MissingServletRequestParameterException::class,
		ServletRequestBindingException::class,
		MissingServletRequestPartException::class,
		ConversionNotSupportedException::class,
		TypeMismatchException::class,
		HttpMessageNotReadableException::class,
		MethodArgumentNotValidException::class,
		BindException::class
	)
	fun parameterException(request: HttpServletRequest, exception: Exception): ResponseEntity<Response> {
		var errorMessage = ErrorCd.INVALID_PARAMETER.message

		// 오류가 발생한 첫번째 파라미터를 에러메시지 끝에 추가함.
		val paramName = getParamNameFromSpringRequestException(exception)
		if (!paramName.isNullOrEmpty()) {
			errorMessage += " : $paramName"
		}

		return handleException(request, exception, ErrorCd.INVALID_PARAMETER, errorMessage)
	}

	/**
	 * spring request exception으로부터 오류가 발생한 첫번째 파라미터명 추출
	 * @param exception
	 * @return
	 */
	private fun getParamNameFromSpringRequestException(exception: Exception): String? {
		if (exception is MissingServletRequestParameterException) {
			return exception.parameterName
		} else if (exception is MissingServletRequestPartException) {
			return exception.requestPartName
		} else if (exception is MethodArgumentNotValidException) {
			val bindingResult: BindingResult = exception.bindingResult
			if (bindingResult.hasFieldErrors()) {
				return Objects.requireNonNull(bindingResult.fieldError).field
			}
		} else if (exception is MethodArgumentTypeMismatchException) {
			return exception.name
		} else if (exception is MethodArgumentConversionNotSupportedException) {
			return exception.name
		} else if (exception is BindException && exception.hasFieldErrors()) {
			// 오류가 발생한 첫번째 파라미터명만 리턴
			return Objects.requireNonNull(exception.fieldError).field
		}

		return null
	}

	/**
	 * spring - no handler
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(NoHandlerFoundException::class)
	fun noHandlerException(request: HttpServletRequest, exception: Exception): ResponseEntity<Response> {
		return handleException(request, exception, ErrorCd.NO_HANDLER_FOUND)
	}

	/**
	 * spring - method not supported
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException::class)
	fun methodNotSupportedException(request: HttpServletRequest, exception: Exception): ResponseEntity<Response> {
		return handleException(request, exception, ErrorCd.METHOD_NOT_ALLOWED)
	}

	/**
	 * spring - not acceptable media type
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
	fun mediaTypeNotAcceptableException(request: HttpServletRequest, exception: Exception): ResponseEntity<Response> {
		return handleException(request, exception, ErrorCd.NOT_ACCEPTABLE)
	}

	/**
	 * spring - unsupported media type
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException::class)
	fun mediaTypeNotSupportedException(request: HttpServletRequest, exception: Exception): ResponseEntity<Response> {
		return handleException(request, exception, ErrorCd.UNSUPPORTED_MEDIA_TYPE)
	}

	/**
	 * spring - no resource found
	 */
	@ExceptionHandler(NoResourceFoundException::class)
	fun noResourceFoundException(request: HttpServletRequest, exception: Exception): ResponseEntity<Response> {
		return handleException(request, exception, ErrorCd.NOT_FOUND)
	}

	/**
	 * 그외
	 * @param request
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Exception::class)
	fun generalException(request: HttpServletRequest, exception: Exception): ResponseEntity<Response> {
		return handleException(request, exception, ErrorCd.INTERNAL_SERVER_ERROR, null, null)
	}

	private fun handleException(request: HttpServletRequest, exception: Exception, errorCd: ErrorCd, errorMessage: String? = null, value: Any? = null): ResponseEntity<Response> {
		if (logger.isErrorEnabled) {
			if (errorCd.httpStatus.is4xxClientError && exception.cause == null) {
				logger.error(
					"{} {}, parameter:{}, status:{}, exception:{}",
					request.method, request.requestURI, paramsToStr(request), errorCd.httpStatus.value(), exception.toString()
				)
			} else {
				logger.error(
					"{} {}, parameter:{}, status:{}, exception:{}",
					request.method, request.requestURI, paramsToStr(request), errorCd.httpStatus.value(), exception.toString(), exception
				)
			}
		}

		request.setAttribute(ERROR_KEY, true)
		val response = Response(errorCd.name, Objects.toString(errorMessage, errorCd.message), value)
		return ResponseEntity(response, errorCd.httpStatus)
	}

	/**
	 * 에러 발생시 응답 body
	 * @param code
	 * @param message
	 * @param value
	 */
	@JvmRecord
	data class Response(val code: String, val message: String, val value: Any?)
	companion object {
		val ERROR_KEY: String = RestExceptionHandler::class.java.name
	}

	fun paramsToStr(request: HttpServletRequest): String? {
		val params = request.parameterMap ?: return null
		val sb = StringBuilder()

		sb.append("{")
		for ((key, value) in params) {
			sb.append(key).append("=").append(value.contentToString()).append(",")
		}
		sb.append("}")
		return sb.toString()
	}
}

