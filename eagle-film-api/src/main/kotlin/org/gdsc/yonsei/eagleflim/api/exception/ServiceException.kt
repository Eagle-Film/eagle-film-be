package org.gdsc.yonsei.eagleflim.api.exception

class ServiceException(
	val errorCode: ErrorCd,
	override val message: String = errorCode.message,
	val debugMessage: String = ""
) : RuntimeException()
