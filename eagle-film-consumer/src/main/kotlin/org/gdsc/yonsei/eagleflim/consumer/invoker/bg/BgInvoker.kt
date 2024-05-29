package org.gdsc.yonsei.eagleflim.consumer.invoker.bg

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
	fun sendRequest(requestUrl: String): ByteArray? {
		val param = mapOf(
			"image_url" to requestUrl,
			"size" to "auto",
			"type" to "person",
		)

		return invoke(param, object : ParameterizedTypeReference<ByteArray>() { })
	}

	private fun <T> invoke(param: Map<String, Any>, type: ParameterizedTypeReference<T>): T? {
		val requestSpec = bgRestClient.method(HttpMethod.POST)
			.uri(url)
			.header("X-Api-Key", token)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.IMAGE_PNG)
			.body(param)

		val result = requestSpec
			.retrieve()
			.onStatus(HttpStatusCode::isError) {
				_, response -> run {
					logger.error("[BgInvoker] API Call Failed - statusCode: {}, message: {}", response.statusCode, response.body)
				}
			}
			.body(type)

		return result
	}

	companion object {
		val bgRestClient: RestClient = RestClient.builder()
			.build()

		val logger: Logger = LoggerFactory.getLogger(BgInvoker::class.java)
	}
}
