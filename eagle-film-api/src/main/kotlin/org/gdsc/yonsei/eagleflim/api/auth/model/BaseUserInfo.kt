package org.gdsc.yonsei.eagleflim.api.auth.model

import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider

data class BaseUserInfo(
	val oauthProvider: OAuthProvider,
	val userIdentifier: String,
	val userName: String
)
