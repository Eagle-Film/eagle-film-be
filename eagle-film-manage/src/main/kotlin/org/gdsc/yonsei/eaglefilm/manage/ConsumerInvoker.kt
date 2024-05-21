package org.gdsc.yonsei.eaglefilm.manage

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ConsumerInvoker(
	@Value("\${consumer.address}") private val baseUrl: String,
) {
	fun registerNode(url: String) {
		val param = mapOf("address" to url)
		invoke(ConsumerCommand.REGISTER_NODE, param, object : ParameterizedTypeReference<Unit>() { })
	}

	fun deleteNode(url: String) {
		val param = mapOf("address" to url)
		invoke(ConsumerCommand.DELETE_NODE, param, object : ParameterizedTypeReference<Unit>() { })
	}

	fun getNodeList(): List<Map<String, String>>? {
		return invoke(ConsumerCommand.SCAN_NODE, null, object : ParameterizedTypeReference<List<Map<String, String>>>() {})
	}

	fun <T> invoke(consumerCommand: ConsumerCommand, param: Map<String, Any>?, type: ParameterizedTypeReference<T>): T? {
		var requestSpec = consumerRestClient.method(consumerCommand.httpMethod)
			.uri(baseUrl + "/" + consumerCommand.location)
			.contentType(MediaType.APPLICATION_JSON)

		param?.let {
			requestSpec = requestSpec.body(it)
		}

		return requestSpec
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError) {
					_, response -> logger.warn("invoke failed - statusCode: {}", response.statusCode)
			}
			.body(type)
	}

	companion object {
		val consumerRestClient: RestClient = RestClient.builder()
			.build()

		val logger = LoggerFactory.getLogger(ConsumerInvoker::class.java)
	}
}

enum class ConsumerCommand(
	val location: String,
	val httpMethod: HttpMethod
) {
	REGISTER_NODE("/consumer/v1/manage/node", HttpMethod.POST),
	DELETE_NODE("/consumer/v1/manage/node", HttpMethod.DELETE),
	SCAN_NODE("/consumer/v1/manage/status", HttpMethod.GET),
}
