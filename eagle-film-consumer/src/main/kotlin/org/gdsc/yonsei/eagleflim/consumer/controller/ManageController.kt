package org.gdsc.yonsei.eagleflim.consumer.controller

import org.gdsc.yonsei.eagleflim.consumer.controller.model.NodeInput
import org.gdsc.yonsei.eagleflim.consumer.model.NodeInfo
import org.gdsc.yonsei.eagleflim.consumer.service.NodeService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/consumer/v1/manage")
class ManageController(
	private val nodeService: NodeService
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
}
