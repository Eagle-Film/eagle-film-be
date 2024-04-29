package org.gdsc.yonsei.eagleflim.api.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.gdsc.yonsei.eagleflim.api.auth.JWTUtil
import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.exception.ServiceException
import org.gdsc.yonsei.eagleflim.api.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtInterceptor(
	val userRepository: UserRepository,
	val objectMapper: ObjectMapper
) : HandlerInterceptor {
	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		val totalToken = request.getHeader("Authorization")
		if (!totalToken.startsWith("Bearer ")) {
			error("Invalid Token")
		}

		val accessToken = totalToken.substringAfter("Bearer ")
		val userIdFromToken = JWTUtil.getUserIdFromToken(accessToken)

		val result = userRepository.getUser(userIdFromToken)
		result ?: run {
			val exception = ServiceException(ErrorCd.UNAUTHORIZED, "Unauthorized")
			response.status = HttpStatus.UNAUTHORIZED.value()
			response.writer.write(objectMapper.writeValueAsString(exception))
			return false
		}

		request.setAttribute("id", result.userId.toHexString())
		return true
	}
}
