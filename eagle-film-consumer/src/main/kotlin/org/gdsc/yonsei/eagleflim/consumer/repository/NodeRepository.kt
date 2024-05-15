package org.gdsc.yonsei.eagleflim.consumer.repository

import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class NodeRepository(
	private val stringRedisTemplate: RedisTemplate<String, String>,
	private val nodeInfoRedisTemplate: RedisTemplate<String, NodeInfo>
) {
	fun insertNode(address: String) {
		stringRedisTemplate.opsForSet().add(REDIS_LABEL_NODE_ADDRESS, address)
		nodeInfoRedisTemplate.opsForValue().set(REDIS_LABLE_NODE_INFO_FORMAT.format(address), NodeInfo(address))
	}

	fun removeNode(address: String) {
		stringRedisTemplate.opsForSet().remove(REDIS_LABEL_NODE_ADDRESS, address)
		nodeInfoRedisTemplate.opsForValue().getAndDelete(REDIS_LABLE_NODE_INFO_FORMAT.format(address))
	}

	fun selectAllNodes(): Set<String> {
		return stringRedisTemplate.opsForSet().members(REDIS_LABEL_NODE_ADDRESS) ?: setOf()
	}

	fun checkNode(address: String): Boolean {
		return stringRedisTemplate.opsForSet().isMember(REDIS_LABEL_NODE_ADDRESS, address)!!
	}

	fun getNodeInfo(address: String): NodeInfo? {
		return nodeInfoRedisTemplate.opsForValue().get(REDIS_LABLE_NODE_INFO_FORMAT.format(address))
	}

	companion object {
		const val REDIS_LABEL_NODE_ADDRESS: String = "Node_Address" // 전체 노드
		const val REDIS_LABLE_NODE_INFO_FORMAT: String = "Node_Info_%s" // Request 할당 된 Node 및 Pair 된 Request 정보
	}
}
