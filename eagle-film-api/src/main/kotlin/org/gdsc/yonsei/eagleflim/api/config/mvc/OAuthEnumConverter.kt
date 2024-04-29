package org.gdsc.yonsei.eagleflim.api.config.mvc

import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider
import org.springframework.core.convert.converter.Converter

class OAuthEnumConverter: Converter<String, OAuthProvider> {
	override fun convert(p0: String): OAuthProvider {
		return OAuthProvider.codeOf(p0)
	}
}
