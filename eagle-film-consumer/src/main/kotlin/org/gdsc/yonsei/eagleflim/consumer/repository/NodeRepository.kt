package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrDefault

@Component
class NodeRepository(
	private val stringRedisTemplate: RedisTemplate<String, String>,
	private val nodeInfoRedisRepository: NodeInfoRedisRepository
) {
	fun insertNode(address: String) {
		stringRedisTemplate.opsForSet().add(REDIS_LABEL_NODE_ADDRESS, address)
		nodeInfoRedisRepository.save(NodeInfo(address))
	}

	fun removeNode(address: String) {
		stringRedisTemplate.opsForSet().remove(REDIS_LABEL_NODE_ADDRESS, address)
		nodeInfoRedisRepository.deleteById(address)
	}

	fun selectAllNodes(): Set<String> {
		return stringRedisTemplate.opsForSet().members(REDIS_LABEL_NODE_ADDRESS) ?: setOf()
	}

	fun checkNode(address: String): Boolean {
		return stringRedisTemplate.opsForSet().isMember(REDIS_LABEL_NODE_ADDRESS, address)!!
	}

	fun getNodeInfo(address: String): NodeInfo? {
		return nodeInfoRedisRepository.findById(address).getOrDefault(null)
	}

	fun updateNodeInfo(nodeInfo: NodeInfo) {
		nodeInfoRedisRepository.save(nodeInfo)
	}

	fun insertDeleteQueue(address: String) {
		stringRedisTemplate.opsForSet().add(REDIS_DELETE_QUEUE, address)
	}

	fun removeDeleteQueue(address: String) {
		stringRedisTemplate.opsForSet().remove(REDIS_DELETE_QUEUE, address)
	}

	fun checkDeleteQueue(address: String): Boolean {
		return stringRedisTemplate.opsForSet().isMember(REDIS_DELETE_QUEUE, address) ?: false
	}

	fun selectAllDeleteQueue(): List<String> {
		return stringRedisTemplate.opsForSet().members(REDIS_DELETE_QUEUE)?.toList() ?: listOf()
	}

	companion object {
		const val REDIS_LABEL_NODE_ADDRESS: String = "nodeAddress" // 전체 노드
		const val REDIS_DELETE_QUEUE: String = "deleteNodeQueue"
	}
}
