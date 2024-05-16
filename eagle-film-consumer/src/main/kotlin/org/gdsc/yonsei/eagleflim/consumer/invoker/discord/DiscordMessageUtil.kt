package org.gdsc.yonsei.eagleflim.consumer.invoker.discord

object DiscordMessageUtil {
	fun registerNodeMessage(nodeUrl: String, allNodeList: List<String>): String {
		return """
			**GPU Node registered**
			---
			serverName: $nodeUrl
			allNodeList: $allNodeList
		""".trimIndent()
	}

	fun removeNodeMessage(nodeUrl: String, allNodeList: List<String>): String {
		return """
			**GPU Node removed**
			---
			serverName: $nodeUrl
			allNodeList: $allNodeList
		""".trimIndent()
	}

	fun createNodeInternalServerMessage(nodeUrl: String): String {
		return """
			**GPU Node API Call Failed (Internal Server Error)**
			---
			serverName: $nodeUrl
		""".trimIndent()
	}

	fun nodeValidateFailedMessage(nodeUrl: String, errorMessage: String): String {
		return """
			**Inconsistent GPU data found** (Warning: GPU Node connection info will be removed from db.)
			---
			serverName: $nodeUrl
			errorMessage: $errorMessage
		""".trimIndent()
	}

	fun warnDanglingRequestMessage(requestId: String): String {
		return """
			**WARNING: Dangling Request Exist. Please retry asap.**
			---
			requestId: $requestId
		""".trimIndent()
	}

	fun jobFinished(requestId: String): String {
		return """
			**Request Finished!!**
			---
			requestId: $requestId
		""".trimIndent()
	}
}
