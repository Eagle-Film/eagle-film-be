package org.gdsc.yonsei.eagleflim.consumer.exception

import org.springframework.http.HttpStatusCode

class HttpInvokerException(
	status: HttpStatusCode,
	url: String,
	params: Map<String, Any>? = null
): RuntimeException("Http Invoker Failed - status: $status, url: $url, params: $params")

