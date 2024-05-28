package org.gdsc.yonsei.eagleflim.consumer.controller.model

import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo

data class NodeStatusOutput(
	val nodeInfo: NodeInfo,
	val requestInfo: PhotoRequest?,
	val userInfo: User?
) {
}
