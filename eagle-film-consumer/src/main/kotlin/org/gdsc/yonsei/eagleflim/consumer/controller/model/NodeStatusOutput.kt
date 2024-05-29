package org.gdsc.yonsei.eagleflim.consumer.controller.model

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.common.model.SimplePhotoRequestInfo
import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo

data class NodeStatusOutput(
	val nodeInfo: NodeInfo,
	val requestInfo: SimplePhotoRequestInfo?,
	val userInfo: User?
) {
}
