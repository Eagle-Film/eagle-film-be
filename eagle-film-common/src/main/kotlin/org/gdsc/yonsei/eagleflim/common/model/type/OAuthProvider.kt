package org.gdsc.yonsei.eagleflim.common.model.type

enum class OAuthProvider(
	val code: String
) {
	KAKAO("kakao"),
	NAVER("naver"),
	GOOGLE("google");

	companion object {
		fun codeOf(code: String): OAuthProvider {
			return entries.first { provider ->
				provider.code == code
			}
		}
	}
}
