package org.gdsc.yonsei.eaglefilm.manage

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SchedulingDiscordMessageSender(
	val discordConfig: DiscordConfig,
	val consumerInvoker: ConsumerInvoker,
	@Value("\${discord.alarmChannel}") private val channelId: String,
) {
	@Scheduled(fixedDelay = 300000)
	fun test() {
		if (!scheduleOn) {
			return
		}

		val jda = discordConfig.jda!!
		jda.textChannels.find { it.id == channelId }?.let {
			val waitingList = consumerInvoker.getWaitingList()
			val nodeInfoList = consumerInvoker.getNodeList() ?: listOf()

			val result = """
				<-- status -->
				queueSize: ${waitingList.size}
				registeredNodeSize: ${nodeInfoList.size}
				registeredNodeInfo: $nodeInfoList
			""".trimIndent()

			it.sendMessage(result).queue()
		}
	}

	companion object {
		var scheduleOn = false
	}
}
