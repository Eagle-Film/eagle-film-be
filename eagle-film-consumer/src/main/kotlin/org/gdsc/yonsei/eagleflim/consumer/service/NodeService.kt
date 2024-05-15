package org.gdsc.yonsei.eagleflim.consumer.service

import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordInvoker
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordMessageUtil
import org.gdsc.yonsei.eagleflim.consumer.repository.NodeRepository
import org.springframework.stereotype.Service

@Service
class NodeService(
	private val nodeRepository: NodeRepository,
	private val discordInvoker: DiscordInvoker
) {
	fun addNode(address: String) {
		if (nodeRepository.checkNode(address)) {
			error("node already contained")
		}

		nodeRepository.insertNode(address)
		val nodeList = nodeRepository.selectAllNodes().toList()
		discordInvoker.sendMessage(DiscordMessageUtil.registerNodeMessage(address, nodeList))
	}

	fun removeNode(address: String) {
		if (!nodeRepository.checkNode(address)) {
			error("node not exist")
		}

		nodeRepository.removeNode(address)
		val nodeList = nodeRepository.selectAllNodes().toList()
		discordInvoker.sendMessage(DiscordMessageUtil.removeNodeMessage(address, nodeList))
	}
}
