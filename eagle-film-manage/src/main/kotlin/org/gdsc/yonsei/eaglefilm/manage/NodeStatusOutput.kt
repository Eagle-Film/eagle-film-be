package org.gdsc.yonsei.eaglefilm.manage

import org.gdsc.yonsei.eagleflim.common.entity.PhotoRequest
import org.gdsc.yonsei.eagleflim.common.entity.User

data class NodeStatusOutput(
	val nodeInfo: NodeInfo? = null,
	val requestInfo: PhotoRequest? = null,
	val userInfo: User? = null
)
