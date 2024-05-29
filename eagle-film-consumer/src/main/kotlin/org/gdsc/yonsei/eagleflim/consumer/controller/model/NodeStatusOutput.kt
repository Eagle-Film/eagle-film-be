package org.gdsc.yonsei.eagleflim.consumer.controller.model

import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo

data class NodeStatusOutput(
	val nodeInfo: NodeInfo,
	val requestId: String?,
) {
}
