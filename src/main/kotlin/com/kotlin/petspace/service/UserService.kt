package com.kotlin.petspace.service

import com.kotlin.petspace.dto.UserLoginRequestDTO
import com.kotlin.petspace.dto.UserLoginResponseDTO
import com.kotlin.petspace.dto.UserRequestDTO
import com.kotlin.petspace.dto.UserResponseDTO
import org.springframework.stereotype.Service

@Service
class UserService {
    fun loginUser(userLogin: UserLoginRequestDTO): UserLoginResponseDTO {
        TODO("Not yet implemented")
    }

    fun registerUser(newUser: UserRequestDTO): UserLoginResponseDTO {
        TODO("Not yet implemented")
    }

    fun updateUser(userId: Long, updatedUser: UserRequestDTO): UserResponseDTO {
        TODO("Not yet implemented")
    }

    fun searchUserById(userId: Long): UserResponseDTO {
        TODO("Not yet implemented")
    }

    fun deleteUserById(userId: Long) {
        TODO("Not yet implemented")
    }
}