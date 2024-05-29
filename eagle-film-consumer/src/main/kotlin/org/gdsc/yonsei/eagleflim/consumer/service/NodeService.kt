package org.gdsc.yonsei.eagleflim.consumer.service

import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordInvoker
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordMessageUtil
import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
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
		val nodeInfo = nodeRepository.getNodeInfo(address) ?: error("node not exist")

		if (nodeInfo.assignedRequest != null) {
			nodeRepository.insertDeleteQueue(address)
			return
		}

		nodeRepository.removeNode(address)
		val nodeList = nodeRepository.selectAllNodes().toList()
		discordInvoker.sendMessage(DiscordMessageUtil.removeNodeMessage(address, nodeList))
	}

	fun getAllNodeStatus(): List<NodeInfo> {
		val nodeList = nodeRepository.selectAllNodes()
		return nodeList.mapNotNull {
			nodeRepository.getNodeInfo(it)
		}.toList()
	}
}
