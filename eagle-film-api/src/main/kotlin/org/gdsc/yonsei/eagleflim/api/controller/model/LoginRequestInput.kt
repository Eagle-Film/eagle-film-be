package org.gdsc.yonsei.eagleflim.api.controller.model

data class LoginRequestInput(
	val loginType: LoginType,

)

enum class LoginType {
	LOGIN, REFRESH
}
