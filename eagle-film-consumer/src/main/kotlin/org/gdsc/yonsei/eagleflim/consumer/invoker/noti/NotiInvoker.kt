package org.gdsc.yonsei.eagleflim.consumer.invoker.noti

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class NotiInvoker(
	@Value("\${noti.url}") private val notiUrl: String,
) {
	fun sendNoti(notiToken: String) {
		discordRestClient.method(HttpMethod.POST)
			.uri(notiUrl + notiToken)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.body<Unit>()
	}

	companion object {
		val discordRestClient: RestClient = RestClient.builder()
			.build()
	}
}
