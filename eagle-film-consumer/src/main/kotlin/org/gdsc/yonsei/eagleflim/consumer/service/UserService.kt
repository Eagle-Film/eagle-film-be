package org.gdsc.yonsei.eagleflim.consumer.service

import org.gdsc.yonsei.eagleflim.common.entity.User
import org.gdsc.yonsei.eagleflim.consumer.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun removeUser(userId: String): Long {
        return userRepository.removeUser(userId)
    }

    fun searchUser(userName: String): List<User> {
        return userRepository.findByUserName(userName)
    }

    fun getUser(userId: String): User? {
        return userRepository.findById(userId)
    }
}
