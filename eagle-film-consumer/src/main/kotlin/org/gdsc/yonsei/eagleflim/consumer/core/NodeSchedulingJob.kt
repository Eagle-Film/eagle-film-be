package org.gdsc.yonsei.eagleflim.consumer.core

import org.gdsc.yonsei.eagleflim.consumer.invoker.NodeInvoker
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordInvoker
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordMessageUtil
import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
import org.gdsc.yonsei.eagleflim.consumer.repository.NodeRepository
import org.gdsc.yonsei.eagleflim.consumer.repository.RequestRepository
import org.gdsc.yonsei.eagleflim.consumer.service.NodeService
import org.gdsc.yonsei.eagleflim.consumer.service.PhotoRequestService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class NodeSchedulingJob(
	private val nodeRepository: NodeRepository,
	private var nodeInvoker: NodeInvoker,
	private val discordInvoker: DiscordInvoker,
	private val requestRepository: RequestRepository,
	private val requestService: PhotoRequestService,
	private val nodeService: NodeService,
	private val photoRequestService: PhotoRequestService
) {
	@Scheduled(fixedDelay = 10000)
	fun scheduleNode() {
		// 노드 목록 다 가져옴
		// 전부 idle check
		// idle check 와 동시에 redis 값 비교 - 일치하지 않으면 이건 무조건 discord alert 때려야 함
		// 통과 완료 시, 분기 처리
		// idle 상태인 노드들 -> redis message consume 수행 (기본적으로 비신뢰 방향이므로, 완료 후 데이터 삭제)
		// 작업중인 server -> finish check 함 -> finish 인 경우 redis 데이터 변경

		val nodeList = nodeRepository.selectAllNodes().toList()
		val validatedNodeInfoList = validateNodeStatus(nodeList)
		val groupedNodeInfoList = validatedNodeInfoList.groupBy { it.waiting }

		assignWaitingRequest(groupedNodeInfoList.getOrDefault(true, listOf()))
		checkResult(groupedNodeInfoList.getOrDefault(false, listOf()))
	}

	/**
	 * 노드 데이터 정합성 검증
	 * 데이터 정합성 검증에 실패할 시, 에러를 로깅하고 Redis에 해당 서버 데이터를 제거한다.
	 */
	private fun validateNodeStatus(nodeList: List<String>): List<NodeInfo> {
		val nodeInfoList = nodeList.map { nodeRepository.getNodeInfo(it) }

		return (nodeList zip nodeInfoList).filter {
			val nodeAddress = it.first

			try {
				val nodeStatusFromServer = nodeInvoker.checkIdle(nodeAddress)
				val nodeStatusFromRedis = it.second

				if (nodeStatusFromRedis == null || nodeStatusFromRedis.waiting != nodeStatusFromServer) {
					processInconsistentNode(nodeAddress, "Node idle status not matched")
					return@filter false
				}

				return@filter true
			} catch (e: Exception) {
				processInconsistentNode(it.first, "API call failed")
				return@filter false
			}
		}.map { it.second!! }
	}

	private fun assignWaitingRequest(nodeInfoList: List<NodeInfo>) {
		val waitingRequests = requestRepository.selectAllWaitingRequests().toList()
		nodeInfoList.take(min(waitingRequests.size, nodeInfoList.size)).forEachIndexed {
			index, nodeInfo -> photoRequestService.assignRequest(waitingRequests[index], nodeInfo.address)
		}
	}

	private fun checkResult(nodeInfoList: List<NodeInfo>) {
		nodeInfoList.map {
			it to nodeInvoker.checkFinished(it.address)
		}.filter {
			it.second.status
		}.forEach {
			photoRequestService.completeRequest(it.first.assignedRequest!!, it.second.resultImage!!)
		}
	}

	private fun processInconsistentNode(nodeUrl: String, errorMessage: String = "Unknown Error") {
		discordInvoker.sendMessage(DiscordMessageUtil.nodeValidateFailedMessage(nodeUrl, errorMessage))
		nodeRepository.removeNode(nodeUrl)
	}
}
