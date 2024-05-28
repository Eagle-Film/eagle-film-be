package org.gdsc.yonsei.eagleflim.consumer.invoker

import org.springframework.http.HttpMethod

enum class NodeCommand(
	val location: String,
	val httpMethod: HttpMethod
) {
	CHECK_IDLE("v1/gen/checkidle", HttpMethod.GET),
	CHECK_FINISH("v1/gen/finish", HttpMethod.GET),
	INFER_REQUEST("v1/gen/infer", HttpMethod.POST),
}
