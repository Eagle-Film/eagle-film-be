package org.gdsc.yonsei.eagleflim.api.service

import org.gdsc.yonsei.eagleflim.api.auth.JWTUtil
import org.gdsc.yonsei.eagleflim.api.auth.model.BaseUserInfo
import org.gdsc.yonsei.eagleflim.api.auth.model.TokenInfo
import org.gdsc.yonsei.eagleflim.api.invoker.kakao.KakaoInvoker
import org.gdsc.yonsei.eagleflim.api.model.OAuthProperty
import org.gdsc.yonsei.eagleflim.api.model.factory.UserFactory
import org.gdsc.yonsei.eagleflim.api.repository.UserRepository
import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class UserService(
	private val kakaoInvoker: KakaoInvoker,
	private val userRepository: UserRepository,
	private val mongoTemplate: MongoTemplate
) {
	fun login(oauthProperty: OAuthProperty): TokenInfo {
		val userInfo = getUserInfoViaOAuth(oauthProperty.oauthProvider, oauthProperty.code)
		val userSearchResult = getUserByOAuthInfo(userInfo.oauthProvider, userInfo.userIdentifier)
			?: createUser(UserFactory.createUserFromBaseUserInfo(userInfo))

		val accessToken = JWTUtil.generateToken(userSearchResult.userId.toHexString(), JWTUtil.ACCESS_TOKEN_EXPIRATION_TIME)
		val refreshToken = JWTUtil.generateToken(userSearchResult.userId.toHexString(), JWTUtil.REFRESH_TOKEN_EXPIRATION_TIME)

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



	fun createUser(user: org.gdsc.yonsei.eagleflim.common.entity.User): org.gdsc.yonsei.eagleflim.common.entity.User {
		return mongoTemplate.insert(user)
	}

	fun getUserByOAuthInfo(oauthProvider: OAuthProvider, oauthIdentifier: String): org.gdsc.yonsei.eagleflim.common.entity.User? {
		val criteria = Criteria.where("oauthProvider").`is`(oauthProvider)
			.and("oauthIdentifier").`is`(oauthIdentifier)

		return mongoTemplate.findOne(Query.query(criteria), org.gdsc.yonsei.eagleflim.common.entity.User::class.java)
	}
}
