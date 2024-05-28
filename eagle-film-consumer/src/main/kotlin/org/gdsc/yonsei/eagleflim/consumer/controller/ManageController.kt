package org.gdsc.yonsei.eagleflim.consumer.controller

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.consumer.controller.model.*
import org.gdsc.yonsei.eagleflim.consumer.repository.RequestRepository
import org.gdsc.yonsei.eagleflim.consumer.repository.UserRepository
import org.gdsc.yonsei.eagleflim.consumer.service.NodeService
import org.gdsc.yonsei.eagleflim.consumer.service.PhotoRequestService
import org.gdsc.yonsei.eagleflim.consumer.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/consumer/v1/manage")
class ManageController(
	private val nodeService: NodeService,
	private val userService: UserService,
	private val requestService: PhotoRequestService,
	private val requestRepository: RequestRepository,
	private val userRepository: UserRepository
) {
	@PostMapping("/node")
	fun registerNode(@RequestBody nodeInput: NodeInput) {
		nodeService.addNode(nodeInput.address)
	}

	@DeleteMapping("/node")
	fun deleteNode(@RequestBody nodeInput: NodeInput) {
		nodeService.removeNode(nodeInput.address)
	}

	@GetMapping("/status")
	fun scan(): List<NodeStatusOutput> {
		val nodeStatusList = nodeService.getAllNodeStatus()
		return nodeStatusList.map {
			val requestInfo = it.assignedRequest?.let {
				request -> return@let requestRepository.selectOneRequest(request)
			}
			val userId = requestInfo?.userId
			val userInfo = userId?.let {
				user -> return@let userService.getUser(user)
			}

			return@map NodeStatusOutput(it, requestInfo, userInfo)
		}
	}

	@GetMapping("/searchRequest")
	fun searchRequest(@RequestParam requestId: String = ""): RequestUserInfoOutput {
		val request = requestRepository.selectOneRequest(requestId) ?: return RequestUserInfoOutput(null, null)
		val user = userService.getUser(request.userId) ?: return RequestUserInfoOutput(null, null)
		return RequestUserInfoOutput(request, user)
	}

	@PostMapping("/deleteUser")
	fun deleteAccount(@RequestBody deleteUserInput: DeleteUserInput): Long {
		return userService.removeUser(deleteUserInput.userId)
	}

	@GetMapping("/waiting")
	fun waitingStatus(): List<String> {
		return requestRepository.selectAllWaitingRequests().toList()
	}

	@PostMapping("/search")
	fun searchUser(@RequestBody searchInput: SearchInput): List<User> {
		return userService.searchUser(searchInput.userName)
	}

	@PostMapping("/reassignJob")
	fun reassignJob(@RequestBody jobInput: ReassignJobInput) {
		requestService.retryRequest(jobInput.requestId)
	}
}
