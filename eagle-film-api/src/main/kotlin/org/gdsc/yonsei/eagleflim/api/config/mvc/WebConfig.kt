package org.gdsc.yonsei.eagleflim.api.config.mvc

import org.gdsc.yonsei.eagleflim.api.interceptor.JwtInterceptor
import org.gdsc.yonsei.eagleflim.api.resolver.EagleUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebConfig(
	private val eagleUserArgumentResolver: EagleUserArgumentResolver,
	private val jwtInterceptor: JwtInterceptor
) : WebMvcConfigurer {
	override fun addFormatters(registry: FormatterRegistry) {
		super.addFormatters(registry)
		registry.addConverter(OAuthEnumConverter())
	}

	override fun addInterceptors(registry: InterceptorRegistry) {
		super.addInterceptors(registry)
		registry.addInterceptor(jwtInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/api/v1/user/login/**")
	}

	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		super.addArgumentResolvers(resolvers)
		resolvers.add(eagleUserArgumentResolver)
	}

	override fun addCorsMappings(registry: CorsRegistry) {
		// TODO: 개발 과정에서만 전체 해제. 이후에는 CORS 예외 규칙을 완전히 제거함.
		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowCredentials(true)
			.maxAge(3600L)
	}
}
