package org.gdsc.yonsei.eagleflim.api.exception

class ServiceException(private val errorCode: ErrorCd, override val message: String = errorCode.message) : RuntimeException()
