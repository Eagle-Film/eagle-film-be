package org.gdsc.yonsei.eagleflim.common.model.factory

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.common.model.UserInfo

object UserInfoFactory {
	fun ofEntry(user: User): UserInfo {
		return UserInfo(user.userId.toHexString(), user.userName, user.requestStatus, user.createYmdt, user.requestYmdt)
	}
}
