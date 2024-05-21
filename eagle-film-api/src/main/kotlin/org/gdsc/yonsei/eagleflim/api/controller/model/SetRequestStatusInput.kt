package org.gdsc.yonsei.eagleflim.api.controller.model

import org.gdsc.yonsei.eagleflim.common.model.type.RequestStatus

data class SetRequestStatusInput(
	val requestId: String,
	val requestStatus: RequestStatus
)
