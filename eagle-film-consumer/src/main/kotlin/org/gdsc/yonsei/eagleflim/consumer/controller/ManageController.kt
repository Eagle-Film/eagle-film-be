package org.gdsc.yonsei.eagleflim.consumer.controller

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.common.model.SimplePhotoRequestInfo
import org.gdsc.yonsei.eagleflim.common.model.factory.PhotoRequestInfoFactory
import org.gdsc.yonsei.eagleflim.consumer.controller.model.*
import org.gdsc.yonsei.eagleflim.consumer.repository.RequestRepository
import org.gdsc.yonsei.eagleflim.consumer.repository.UserRepository
import org.gdsc.yonsei.eagleflim.consumer.service.NodeService
import org.gdsc.yonsei.eagleflim.consumer.service.PhotoRequestService
import org.gdsc.yonsei.eagleflim.consumer.service.UserService
import org.springframework.web.bind.annotation.*

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
			return@map NodeStatusOutput(it, it.assignedRequest)
		}
	}

	@GetMapping("/searchRequest")
	fun searchRequest(@RequestParam requestId: String = ""): RequestUserInfoOutput {
		val request = requestRepository.selectOneRequest(requestId) ?: return RequestUserInfoOutput(null, null)
		val user = userService.getUser(request.userId) ?: return RequestUserInfoOutput(PhotoRequestInfoFactory.simpleOfEntry(request), null)
		return RequestUserInfoOutput(PhotoRequestInfoFactory.simpleOfEntry(request), user)
	}

	@PostMapping("/deleteUser")
	fun deleteAccount(@RequestBody deleteUserInput: DeleteUserInput): Long {
		return userService.removeUser(deleteUserInput.userId)
	}

	@GetMapping("/waiting")
	fun waitingStatus(): List<SimplePhotoRequestInfo> {
		return requestRepository.selectAllWaitingRequests().mapNotNull {
			requestRepository.selectOneRequest(it)?.let { it1 -> PhotoRequestInfoFactory.simpleOfEntry(it1) }
		}
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
