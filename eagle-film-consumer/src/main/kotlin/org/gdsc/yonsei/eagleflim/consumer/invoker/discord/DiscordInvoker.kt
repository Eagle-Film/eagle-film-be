package org.gdsc.yonsei.eagleflim.consumer.invoker.discord

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class DiscordInvoker(
	@Value("\${discord.url}") private val discordUrl: String,
) {
	fun sendMessage(message: String) {
		val param = mapOf("content" to message)

		discordRestClient.method(HttpMethod.POST)
			.uri(discordUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(param)
			.retrieve()
			.body<Unit>()
	}

	companion object {
		val discordRestClient: RestClient = RestClient.builder()
			.build()
	}
}
