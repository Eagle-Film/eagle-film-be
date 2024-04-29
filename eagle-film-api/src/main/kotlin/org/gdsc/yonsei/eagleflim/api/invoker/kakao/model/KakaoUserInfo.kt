package org.gdsc.yonsei.eagleflim.api.invoker.kakao.model

data class KakaoUserInfo(
	val id: String,
	val properties: Properties
)


data class Properties(val nickname: String = "")
