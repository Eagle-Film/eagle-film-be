package org.gdsc.yonsei.eaglefilm.manage

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class MessageListener(
	val consumerInvoker: ConsumerInvoker
): ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.author.isBot) {
			return
		}

		val message = event.message
		val content = message.contentRaw
		val channel = event.channel

		when {
			content == "!help" -> channel.sendMessage("""
				!정기웅: 정기웅
				!정퀸웅: 정퀸웅
				!ping: pong
				!nodeStatus: 노드의 상태를 출력합니다.
				!registerNode (node address): 노드를 추가합니다.
				!deleteNode (node address): 노드를 제거합니다.
				!waitingRequests : 처리되지 않고 대기중인 요청의 목록을 가져옵니다.
				!searchUser (user name): 유저 정보를 검색합니다. (이름이 겹치는 사용자가 있을 경우, 모두 출력)
				!deleteUser (userId): 해당 유저를 제거합니다.
				!reassignJob (requestId): 실패한 요청을 다시 재시작 합니다. (Queue에 재 할당, 우선순위는 미뤄짐)
			""".trimIndent()).queue()

			content == "!ping" -> channel.sendMessage("뽕~").queue()

			content == "!정기웅" -> {
				channel.sendMessage(
					"""
				우정 이행시
				우: 우~
				정: 정기웅
			""".trimIndent()
				).queue()
			}

			content == "!정퀸웅" -> channel.sendMessage("https://kr.object.ncloudstorage.com/eagle-film-dev/66538d5b72e347617530a8f6").queue()

			content == "!nodeStatus" || content == "!ns" -> {
				val result = consumerInvoker.getNodeList()
				val message = result?.toString() ?: "ERROR"
				channel.sendMessage(message).queue()
			}

			content == "!waitingRequests" || content == "!ws" -> {
				val requests = consumerInvoker.getWaitingList()
				channel.sendMessage(requests.toString()).queue()
			}

			content.startsWith("!registerNode") || content.startsWith("!rn") -> {
				if (content.split(" ").size != 2) {
					channel.sendMessage("invalid request - usage: \"!registerNode {address}\"").queue()
					return
				}
				val address = content.split(" ")[1]
				consumerInvoker.registerNode(address)
			}

			content.startsWith("!deleteNode") || content.startsWith("!dn") -> {
				if (content.split(" ").size != 2) {
					channel.sendMessage("invalid request - usage: \"!deleteNode {address}\"").queue()
					return
				}
				val address = content.split(" ")[1]
				consumerInvoker.deleteNode(address)
			}

			content.startsWith("!searchUser") || content.startsWith("!su") -> {
				if (content.split(" ").size == 1) {
					channel.sendMessage("invalid request - usage: \"!searchUser {userName}\"").queue()
					return
				}
				val userName = content.substring(content.indexOf(" ") + 1)
				val result = consumerInvoker.findUserByName(userName)

				channel.sendMessage(result.toString()).queue()
			}

			content.startsWith("!deleteUser") || content.startsWith("!du") -> {
				if (content.split(" ").size != 2) {
					channel.sendMessage("invalid request - usage: \"!deleteUser {userId}\"").queue()
					return
				}
				val userId = content.split(" ")[1]
				val count = consumerInvoker.deleteUser(userId)
				val content = if (count == 0) "user not exist" else "user delete complete."
				channel.sendMessage(content).queue()
			}

			content.startsWith("!reassignJob") || content.startsWith("!rj") -> {
				if (content.split(" ").size != 2) {
					channel.sendMessage("invalid request - usage: \"!reassignJob {requestId}\"").queue()
					return
				}
				val reassignJob = content.split(" ")[1]
				consumerInvoker.reassignJob(reassignJob)
				channel.sendMessage("OK").queue()
			}

			content.startsWith("!searchRequest") || content.startsWith("sr") -> {
				if (content.split(" ").size != 2) {
					channel.sendMessage("invalid request - usage: \"!searchRequest {requestId}\"").queue()
					return
				}
				val requestId = content.split(" ")[1]
				val result = consumerInvoker.searchRequest(requestId)
				channel.sendMessage(result.toString()).queue()
			}
		}
	}
}
