package org.gdsc.yonsei.eagleflim.api.invoker.kakao

import org.springframework.http.HttpMethod

enum class KakaoCommand(
	val location: String,
	val httpMethod: HttpMethod
) {
	REQUEST_TOKEN("https://kauth.kakao.com/oauth/token", HttpMethod.POST),
	USER_INFO("https://kapi.kakao.com/v2/user/me", HttpMethod.GET)
}
