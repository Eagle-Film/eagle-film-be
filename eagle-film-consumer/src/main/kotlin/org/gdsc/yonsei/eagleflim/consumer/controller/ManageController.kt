package org.gdsc.yonsei.eagleflim.consumer.controller

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.consumer.controller.model.DeleteUserInput
import org.gdsc.yonsei.eagleflim.consumer.controller.model.NodeInput
import org.gdsc.yonsei.eagleflim.consumer.controller.model.SearchInput
import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
import org.gdsc.yonsei.eagleflim.consumer.repository.RequestRepository
import org.gdsc.yonsei.eagleflim.consumer.service.NodeService
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
	private val requestRepository: RequestRepository
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
	fun scan(): List<NodeInfo> {
		return nodeService.getAllNodeStatus()
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
}
