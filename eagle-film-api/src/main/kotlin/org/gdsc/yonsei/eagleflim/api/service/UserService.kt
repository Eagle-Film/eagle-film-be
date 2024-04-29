package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.auth.JWTUtil
import org.gdsc.yonsei.eagleflim.api.auth.model.BaseUserInfo
import org.gdsc.yonsei.eagleflim.api.auth.model.TokenInfo
import org.gdsc.yonsei.eagleflim.api.invoker.kakao.KakaoInvoker
import org.gdsc.yonsei.eagleflim.api.model.OAuthProperty
import org.gdsc.yonsei.eagleflim.api.model.factory.UserFactory
import org.gdsc.yonsei.eagleflim.api.repository.UserRepository
import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider
import org.springframework.stereotype.Service

@Service
class UserService(
	private val kakaoInvoker: KakaoInvoker,
	private val userRepository: UserRepository
) {
	fun login(oauthProperty: OAuthProperty): TokenInfo {
		val userInfo = getUserInfoViaOAuth(oauthProperty.oauthProvider, oauthProperty.code)
		val userSearchResult = userRepository.getUserByOAuthInfo(userInfo.oauthProvider, userInfo.userIdentifier)
			?: userRepository.createUser(UserFactory.createUserFromBaseUserInfo(userInfo))

		val accessToken = JWTUtil.generateToken(userSearchResult.userId, JWTUtil.ACCESS_TOKEN_EXPIRATION_TIME)
		val refreshToken = JWTUtil.generateToken(userSearchResult.userId, JWTUtil.REFRESH_TOKEN_EXPIRATION_TIME)

		// TODO: refresh Token 관리
		return TokenInfo(accessToken, refreshToken)
	}

	private fun getUserInfoViaOAuth(oAuthProvider: OAuthProvider, code: String): BaseUserInfo {
		return when (oAuthProvider) {
			OAuthProvider.KAKAO -> getUserInfoViaKakao(code)
			OAuthProvider.GOOGLE -> getUserInfoViaGoogle(code)
			OAuthProvider.NAVER -> getUserInfoViaNaver(code)
		}
	}

	private fun getUserInfoViaKakao(code: String): BaseUserInfo {
		val kakaoOAuthToken = kakaoInvoker.getTokenFromOAuth(code)
		val kakaoUserInfo = kakaoInvoker.getUserInfo(kakaoOAuthToken.accessToken)

		return BaseUserInfo(OAuthProvider.KAKAO, kakaoUserInfo.id, kakaoUserInfo.properties.nickname)
	}

	private fun getUserInfoViaGoogle(code: String): BaseUserInfo {
		TODO("Not yet implemented")
	}

	private fun getUserInfoViaNaver(code: String): BaseUserInfo {
		TODO("Not yet implemented")
	}
}
