package org.gdsc.yonsei.eagleflim.api.invoker.kakao.model

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoOAuthToken(
	@JsonProperty("token_type")	val tokenType: String,
	@JsonProperty("access_token") val accessToken: String,
	@JsonProperty("id_token") val idToken: String?,
	@JsonProperty("expires_in") val expiresIn: Int,
	@JsonProperty("refresh_token") val refreshToken: String,
	@JsonProperty("refresh_token_expires_in") val refreshTokenExpiresIn: Int,
	@JsonProperty("scope") val scope: String?
)
