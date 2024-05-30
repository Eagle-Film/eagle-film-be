package org.gdsc.yonsei.eagleflim.consumer.exception

import org.springframework.http.HttpStatusCode

class HttpInvokerException(
	val status: HttpStatusCode,
	val url: String,
	val params: Map<String, Any>? = null
): RuntimeException("Http Invoker Failed - status: $status, url: $url, params: $params")
