package org.gdsc.yonsei.eagleflim.api.resolver

import org.gdsc.yonsei.eagleflim.api.auth.annotation.EagleUser
import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.exception.ServiceException
import org.gdsc.yonsei.eagleflim.api.repository.UserRepository
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.gdsc.yonsei.eagleflim.common.model.factory.UserInfoFactory
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class EagleUserArgumentResolver(val userRepository: UserRepository) : HandlerMethodArgumentResolver {
	override fun supportsParameter(parameter: MethodParameter): Boolean {
		return parameter.parameterType == UserInfo::class.java && parameter.hasParameterAnnotation(EagleUser::class.java)
	}

	override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): UserInfo {
		val id = webRequest.getAttribute("id", RequestAttributes.SCOPE_REQUEST) as String
		val user = userRepository.getUser(id) ?: throw ServiceException(ErrorCd.UNAUTHORIZED, "user not found")

		return UserInfoFactory.ofEntry(user)
	}
}
