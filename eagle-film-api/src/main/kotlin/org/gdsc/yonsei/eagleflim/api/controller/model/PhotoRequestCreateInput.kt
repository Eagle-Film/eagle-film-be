package org.gdsc.yonsei.eagleflim.api.controller.model

import org.gdsc.yonsei.eagleflim.common.model.type.ImageProcessType

data class PhotoRequestCreateInput(
	val imageList: List<String>,
	val imageProcessType: ImageProcessType = ImageProcessType.NORMAL
)
