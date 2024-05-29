package org.gdsc.yonsei.eaglefilm.manage

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class BgInvoker(
	@Value("\${bg.url}") private val url: String,
	@Value("\${bg.token}") private val token: String,
) {
	fun getParam(): Map<String, Any>? {
		val requestSpec = bgRestClient.method(HttpMethod.GET)
			.uri(url.replace("removebg", "account"))
			.header("X-Api-Key", token)
			.accept(MediaType.APPLICATION_JSON)

		val result = requestSpec
			.retrieve()
			.onStatus(HttpStatusCode::isError) {
			   _, response -> run {
				   logger.error("[BgInvoker] API Call Failed - statusCode: {}, message: {}", response.statusCode, response.body)
			   }
			}
			.body(object: ParameterizedTypeReference<Map<String, Any>>() {})

		return result
	}

	companion object {
		val bgRestClient: RestClient = RestClient.builder()
			.build()

		val logger: Logger = LoggerFactory.getLogger(BgInvoker::class.java)
	}
}
