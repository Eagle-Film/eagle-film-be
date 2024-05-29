package org.gdsc.yonsei.eagleflim.consumer.invoker

import org.gdsc.yonsei.eagleflim.common.model.type.ImageProcessType
import org.gdsc.yonsei.eagleflim.consumer.exception.HttpInvokerException
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordInvoker
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordMessageUtil
import org.gdsc.yonsei.eagleflim.consumer.model.GenerationResult
import org.gdsc.yonsei.eagleflim.consumer.model.NodeIdleInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import java.io.BufferedReader
import java.io.InputStream

@Component
class NodeInvoker(
	private val discordInvoker: DiscordInvoker,
) {
	fun checkIdle(nodeUrl: String): Boolean {
		return invoke(nodeUrl, NodeCommand.CHECK_IDLE, null, null, object : ParameterizedTypeReference<NodeIdleInfo>() {})?.idle ?: false
	}

	fun checkFinished(nodeUrl: String, requestId: String): GenerationResult {
		val urlParam = LinkedMultiValueMap<String, String>()
		urlParam.add("reqId", requestId)
		return invoke(nodeUrl, NodeCommand.CHECK_FINISH, null, urlParam, object : ParameterizedTypeReference<GenerationResult>() {}) ?: error("body is null")
	}

	fun requestInference(nodeUrl: String, requestId: String, photoList: List<String>, imageProcessType: ImageProcessType) {
		val param = mapOf(
			"reqId" to requestId,
			"images" to photoList,
			"gender" to imageProcessType.code
		)

		invoke(nodeUrl, NodeCommand.INFER_REQUEST, param, null, object : ParameterizedTypeReference<Unit>() { })
	}

	private fun <T> invoke(baseUrl: String, nodeCommand: NodeCommand, param: Map<String, Any>?, urlParam: MultiValueMap<String, String>?, type: ParameterizedTypeReference<T>): T? {
		val uri = urlParam?.let { baseUrl + "/" + UriComponentsBuilder.fromUriString(nodeCommand.location).queryParams(it).toUriString() } ?: ("$baseUrl/${nodeCommand.location}")
		val protocol = if (uri.endsWith("443")) "https://" else "http://"

		var requestSpec = nodeRestClient.method(nodeCommand.httpMethod)
			.uri(protocol + uri)
			.contentType(MediaType.APPLICATION_JSON)

		param?.let {
			requestSpec = requestSpec.body(it)
		}

		val result = requestSpec
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError) {
				_, response -> throw HttpInvokerException(response.statusCode, nodeCommand.location, param)
			}
			.onStatus(HttpStatusCode::is5xxServerError) {
				_, response -> run {
					logger.error("[NodeInvoker] API Call Failed. statusCode: {}, body: {}", response.statusCode, readBody(response.body))
					discordInvoker.sendMessage(DiscordMessageUtil.createNodeInternalServerMessage(baseUrl))
				}
			}
			.body(type)

		logger.info("[NodeInvoker] API Call - baseUrl: {}, param: {}, nodeCommand: {}, result: {}", baseUrl, if (nodeCommand == NodeCommand.INFER_REQUEST) null else param, nodeCommand, if (nodeCommand == NodeCommand.CHECK_FINISH) "SKIP" else result)
		return result
	}

	private fun readBody(body: InputStream): String {
		return body.bufferedReader().use(BufferedReader::readText)
	}

	companion object {
		val nodeRestClient: RestClient = RestClient.builder()
			.build()

		val logger: Logger = LoggerFactory.getLogger(NodeInvoker::class.java)
	}
}
