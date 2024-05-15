package org.gdsc.yonsei.eagleflim.consumer.model

data class NodeInfo(
	val address: String,
	val waiting: Boolean = true,
	val assignedRequest: String? = null
)
