package org.gdsc.yonsei.eagleflim.consumer.invoker

import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.model.type.ImageProcessType
import org.gdsc.yonsei.eagleflim.consumer.exception.HttpInvokerException
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordInvoker
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordMessageUtil
import org.gdsc.yonsei.eagleflim.consumer.model.GenerationResult
import org.gdsc.yonsei.eagleflim.consumer.model.NodeIdleInfo
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.retry.RetryContext
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.io.IOException

@Component
class NodeInvoker(
	private val discordInvoker: DiscordInvoker
) {
	fun checkIdle(nodeUrl: String): Boolean {
		return invoke(nodeUrl, NodeCommand.CHECK_IDLE, null, object : ParameterizedTypeReference<NodeIdleInfo>() {})?.idle ?: false
	}

	fun checkFinished(nodeUrl: String): GenerationResult {
		return invoke(nodeUrl, NodeCommand.CHECK_FINISH, null, object : ParameterizedTypeReference<GenerationResult>() {}) ?: error("body is null")
	}

	fun requestInference(nodeUrl: String, photoList: List<String>, imageProcessType: ImageProcessType) {
		val param = mapOf(
			"images" to photoList,
			"gender" to imageProcessType.code
		)

		invoke(nodeUrl, NodeCommand.INFER_REQUEST, param, object : ParameterizedTypeReference<Unit>() { })
	}

	private fun <T> invoke(baseUrl: String, nodeCommand: NodeCommand, param: Map<String, Any>?, type: ParameterizedTypeReference<T>): T? {
		var requestSpec = nodeRestClient.method(nodeCommand.httpMethod)
			.uri(baseUrl + nodeCommand.location)
			.contentType(MediaType.APPLICATION_JSON)

		param?.let {
			requestSpec = requestSpec.body(it)
		}

		return requestSpec
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError) {
				_, response -> throw HttpInvokerException(response.statusCode, nodeCommand.location, param)
			}
			.onStatus(HttpStatusCode::is5xxServerError) {
				_, _ -> discordInvoker.sendMessage(DiscordMessageUtil.createNodeInternalServerMessage(baseUrl))
			}
			.body(type)
	}

	companion object {
		val nodeRestClient: RestClient = RestClient.builder()
			.requestInterceptor(clientHttpRequestInterceptor())
			.build()

		private fun clientHttpRequestInterceptor(): ClientHttpRequestInterceptor {
			return ClientHttpRequestInterceptor { request: HttpRequest?, body: ByteArray?, execution: ClientHttpRequestExecution ->
				val retryTemplate = RetryTemplate()
				retryTemplate.setRetryPolicy(SimpleRetryPolicy(3))
				try {
					return@ClientHttpRequestInterceptor retryTemplate.execute<ClientHttpResponse, IOException> { context: RetryContext? -> execution.execute(request, body) }
				} catch (throwable: Throwable) {
					throw RuntimeException(throwable)
				}
			}
		}
	}
}
