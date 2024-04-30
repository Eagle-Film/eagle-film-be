package org.gdsc.yonsei.eagleflim.api.invoker.kakao

import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.gdsc.yonsei.eagleflim.api.exception.HttpInvokerException
import org.gdsc.yonsei.eagleflim.api.exception.ServiceException
import org.gdsc.yonsei.eagleflim.api.invoker.kakao.model.KakaoOAuthToken
import org.gdsc.yonsei.eagleflim.api.invoker.kakao.model.KakaoUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

@Component
class KakaoInvoker(
	@Value("\${oauth.kakao.clientCode}") private val clientCode: String,
	@Value("\${oauth.kakao.redirectUri}") private val redirectUri: String
) {
	fun getTokenFromOAuth(code: String): KakaoOAuthToken {
		val param = LinkedMultiValueMap<String, Any>().apply {
			setAll(
				mapOf("grant_type" to "authorization_code", "client_id" to clientCode, "redirect_uri" to redirectUri, "code" to code)
			)
		}

		val result = invoke(KakaoCommand.REQUEST_TOKEN, null, param, object: ParameterizedTypeReference<KakaoOAuthToken>() {})
		return result ?: throw ServiceException(ErrorCd.INTERNAL_SERVER_ERROR, "response is null")
	}

	fun getUserInfo(token: String): KakaoUserInfo {
		val result = invoke(KakaoCommand.USER_INFO, token, null, object: ParameterizedTypeReference<KakaoUserInfo>() {})
		return result ?: throw ServiceException(ErrorCd.INTERNAL_SERVER_ERROR, "response is null")
	}

	fun <T> invoke(kakaoCommand: KakaoCommand, header: String?, param: Map<String, Any>?, type: ParameterizedTypeReference<T>): T? {
		var requestSpec = kakaoRestClient.method(kakaoCommand.httpMethod)
			.uri(kakaoCommand.location)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)

		header?.let {
			requestSpec = requestSpec.header("Authorization", "Bearer $header")
		}

		param?.let {
			requestSpec = requestSpec.body(it)
		}

		return requestSpec
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError) {
				_, response -> throw HttpInvokerException(response.statusCode, kakaoCommand.location, param)
			}
			.body(type)
	}

	companion object {
		val kakaoRestClient: RestClient = RestClient.builder()
			.build()
	}
}
