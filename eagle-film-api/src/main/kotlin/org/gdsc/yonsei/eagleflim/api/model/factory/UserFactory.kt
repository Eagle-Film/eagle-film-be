package org.gdsc.yonsei.eagleflim.api.model.factory

import org.gdsc.yonsei.eagleflim.api.auth.model.BaseUserInfo
import org.gdsc.yonsei.eagleflim.common.entity.User

object UserFactory {
	fun createUserFromBaseUserInfo(baseUserInfo: BaseUserInfo): User {
		return User(userName = baseUserInfo.userName, oauthProvider = baseUserInfo.oauthProvider, oauthIdentifier = baseUserInfo.userIdentifier)
	}
}
