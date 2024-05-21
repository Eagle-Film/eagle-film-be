package org.gdsc.yonsei.eaglefilm.manage

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class MessageListener(
	val consumerInvoker: ConsumerInvoker
): ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.author.isBot) {
			return
		}

		val message = event.message
		val content = message.contentRaw


		if (content == "!ping") {
			val channel = event.channel
			channel.sendMessage("뽕~").queue()
		}

		if (content == "!정기웅") {
			val channel = event.channel
			channel.sendMessage("""
				우정 이행시
				우: 우~
				정: 정기웅
			""".trimIndent()).queue()
		}

		if (content.startsWith("!registerNode")) {
			val channel = event.channel
			if (content.split(" ").size != 2) {
				channel.sendMessage("invalid request - usage: \"!registerNode {address}\"").queue()
				return
			}
			val address = content.split(" ")[1]
			consumerInvoker.registerNode(address)
		}

		if (content == "!nodeStatus") {
			val channel = event.channel
			val result = consumerInvoker.getNodeList()
			val message = result?.toString() ?: "ERROR"
			channel.sendMessage(message).queue()
		}

		if (content.startsWith("!deleteNode")) {
			val channel = event.channel
			if (content.split(" ").size != 2) {
				channel.sendMessage("invalid request - usage: \"!deleteNode {address}\"").queue()
				return
			}
			val address = content.split(" ")[1]
			consumerInvoker.deleteNode(address)
		}
	}
}
