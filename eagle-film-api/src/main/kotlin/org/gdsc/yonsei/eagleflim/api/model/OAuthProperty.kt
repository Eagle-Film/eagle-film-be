package org.gdsc.yonsei.eagleflim.api.model

import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider


data class OAuthProperty(
	val oauthProvider: OAuthProvider,
	val code: String,
	val additionalParam: Map<String, String>
)
