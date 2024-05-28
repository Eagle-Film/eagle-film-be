package org.gdsc.yonsei.eaglefilm.manage

data class NodeInfo(
	val address: String = "",
	val waiting: Boolean = true,
	val assignedRequest: String? = null
)
