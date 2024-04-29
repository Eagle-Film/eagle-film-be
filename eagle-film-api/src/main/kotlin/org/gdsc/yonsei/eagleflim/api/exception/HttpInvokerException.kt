package org.gdsc.yonsei.eagleflim.api.exception

import org.springframework.http.HttpStatusCode

class HttpInvokerException(
	status: HttpStatusCode,
	url: String,
	params: Map<String, Any>? = null
): RuntimeException()
