package org.gdsc.yonsei.eaglefilm.manage

import org.gdsc.yonsei.eagleflim.common.entity.User
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
		invoke(ConsumerCommand.REGISTER_NODE, param, object : ParameterizedTypeReference<Unit>() {})
	}

	fun findUserByName(userName: String): List<User> {
		val param = mapOf("userName" to userName)
		return invoke(ConsumerCommand.SEARCH_USER, param, object : ParameterizedTypeReference<List<User>>() {}) ?: listOf()
	}

	fun deleteNode(url: String) {
		val param = mapOf("address" to url)
		invoke(ConsumerCommand.DELETE_NODE, param, object : ParameterizedTypeReference<Unit>() {})
	}

	fun getNodeList(): List<Map<String, String>>? {
		return invoke(ConsumerCommand.SCAN_NODE, null, object : ParameterizedTypeReference<List<Map<String, String>>>() {})
	}

	fun getWaitingList(): List<String> {
		return invoke(ConsumerCommand.WAITING_LIST, null,
			object : ParameterizedTypeReference<List<String>>() {
			} ) ?: listOf()
	}

	fun deleteUser(userId: String): Int {
		val param = mapOf("userId" to userId)
		return invoke(ConsumerCommand.DELETE_USER, param, object : ParameterizedTypeReference<Int>() {}) ?: 0
	}

	fun reassignJob(requestId: String) {
		val param = mapOf("requestId" to requestId)
		invoke(ConsumerCommand.REASSIGN_JOB, param, object : ParameterizedTypeReference<Unit>() {})
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
			.onStatus(HttpStatusCode::is4xxClientError) { _, response ->
				logger.warn("invoke failed - statusCode: {}", response.statusCode)
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
	REGISTER_NODE("consumer/v1/manage/node", HttpMethod.POST),
	DELETE_NODE("consumer/v1/manage/node", HttpMethod.DELETE),
	SCAN_NODE("consumer/v1/manage/status", HttpMethod.GET),
	DELETE_USER("consumer/v1/manage/deleteUser", HttpMethod.POST),
	WAITING_LIST("consumer/v1/manage/waiting", HttpMethod.GET),
	SEARCH_USER("consumer/v1/manage/search", HttpMethod.POST),
	REASSIGN_JOB("consumer/v1/manage/reassignJob", HttpMethod.POST)
}
