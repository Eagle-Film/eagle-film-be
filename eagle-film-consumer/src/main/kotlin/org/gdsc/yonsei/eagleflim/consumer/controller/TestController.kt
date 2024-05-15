package org.gdsc.yonsei.eagleflim.consumer.controller

import org.gdsc.yonsei.eagleflim.consumer.controller.model.DiscordInput
import org.gdsc.yonsei.eagleflim.consumer.invoker.discord.DiscordInvoker
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/consumer/v1/test")
class TestController(
	private val discordInvoker: DiscordInvoker
) {
	@PostMapping("/discord")
	fun sendMessageToDiscord(@RequestBody discordInput: DiscordInput) {
		discordInvoker.sendMessage(discordInput.message)
	}
}
