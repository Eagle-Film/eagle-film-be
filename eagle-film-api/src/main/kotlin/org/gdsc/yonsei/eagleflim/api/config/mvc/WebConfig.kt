package org.gdsc.yonsei.eagleflim.api.config.mvc

import org.gdsc.yonsei.eagleflim.api.interceptor.JwtInterceptor
import org.gdsc.yonsei.eagleflim.api.resolver.EagleUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebConfig(
	val eagleUserArgumentResolver: EagleUserArgumentResolver,
	val jwtInterceptor: JwtInterceptor
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
}
