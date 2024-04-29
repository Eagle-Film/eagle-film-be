package org.gdsc.yonsei.eagleflim.api.controller

import org.gdsc.yonsei.eagleflim.api.auth.annotation.EagleUser
import org.gdsc.yonsei.eagleflim.api.auth.model.TokenInfo
import org.gdsc.yonsei.eagleflim.api.invoker.kakao.KakaoInvoker
import org.gdsc.yonsei.eagleflim.api.model.OAuthProperty
import org.gdsc.yonsei.eagleflim.api.service.UserService
import org.gdsc.yonsei.eagleflim.common.model.UserInfo
import org.gdsc.yonsei.eagleflim.common.model.type.OAuthProvider
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(val userService: UserService, private val kakaoInvoker: KakaoInvoker) {
	@GetMapping("/login/{provider}")
	fun login(
		@PathVariable provider: OAuthProvider,
		@RequestParam code: String,
		@RequestParam additionalParam: Map<String, String>
	): TokenInfo {
		return userService.login(OAuthProperty(provider, code, additionalParam))
	}

	@GetMapping("/info")
	fun info(@EagleUser user: UserInfo): UserInfo {
		return user
	}

	@DeleteMapping("/withdraw")
	fun withdraw() {

	}
}
