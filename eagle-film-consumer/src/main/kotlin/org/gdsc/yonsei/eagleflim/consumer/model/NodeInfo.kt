package org.gdsc.yonsei.eagleflim.consumer.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "nodeInfo")
data class NodeInfo(
	@Id val address: String,
	val waiting: Boolean = true,
	val assignedRequest: String? = null
)
