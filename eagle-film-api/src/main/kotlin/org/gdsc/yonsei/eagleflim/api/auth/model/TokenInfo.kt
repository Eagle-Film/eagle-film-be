package org.gdsc.yonsei.eagleflim.api.auth.model

data class TokenInfo(
	val accessToken: String,
	val refreshToken: String
)
