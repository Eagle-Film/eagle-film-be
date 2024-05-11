package org.gdsc.yonsei.eagleflim.api.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.gdsc.yonsei.eagleflim.api.auth.JWTUtil
import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtInterceptor(
	val userRepository: UserRepository,
	val objectMapper: ObjectMapper
) : HandlerInterceptor {
	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		if (HttpMethod.OPTIONS.matches(request.method)) {
			return true
		}

		val totalToken = request.getHeader("Authorization")
		if (totalToken.isNullOrEmpty() || !totalToken.startsWith("Bearer ")) {
			response.status = 403
			return false
		}
		logger.info("Authorization Header: {}", totalToken)

		try {
			val accessToken = totalToken.substringAfter("Bearer ")
			val userIdFromToken = JWTUtil.getUserIdFromToken(accessToken)

			val result = userRepository.getUser(userIdFromToken)
			result ?: throw ErrorCd.UNAUTHORIZED.serviceException("Unauthorized")

			request.setAttribute("id", result.userId)
			return true
		} catch (e: Exception) {
			response.status = 403
			return false
		}
	}

	companion object {
		val logger: Logger = LoggerFactory.getLogger(JwtInterceptor::class.java)
	}
}
