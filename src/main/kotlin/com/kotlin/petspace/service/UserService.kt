package com.kotlin.petspace.service

import com.kotlin.basecomponents.exceptions.ElementNotFoundException
import com.kotlin.basecomponents.exceptions.ValidationException
import com.kotlin.petspace.dto.InstitutionResponseDTO
import com.kotlin.petspace.dto.PersonResponseDTO
import com.kotlin.petspace.dto.UserLoginRequestDTO
import com.kotlin.petspace.dto.UserLoginResponseDTO
import com.kotlin.petspace.dto.UserRequestDTO
import com.kotlin.petspace.dto.UserResponseDTO
import com.kotlin.petspace.model.Institution
import com.kotlin.petspace.model.Person
import com.kotlin.petspace.model.User
import com.kotlin.petspace.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val petService: PetService
) {

    fun loginUser(userLogin: UserLoginRequestDTO): UserLoginResponseDTO {
        val user = searchUserByMail(userLogin.userEmail)
        user.validatePassword(user.password!!, userLogin.password)
        return UserLoginResponseDTO.toDTO(user)
    }

    fun registerUser(newUser: UserRequestDTO): UserLoginResponseDTO {
        if (alreadyRegistered(newUser.userEmail)) throw ValidationException("Mail already registered")
        val user = saveUser(newUser.toUser())
        return UserLoginResponseDTO.toDTO(user)
    }

    fun updateUser(userId: Long, updatedUser: UserRequestDTO) {
        val actual = searchUserById(userId)
        val updated = updatedUser.toUser()
        if (actual.getIsPerson() != updated.getIsPerson()) throw ValidationException("Error on user type")
        updated.id = actual.id
        updated.password = actual.password
        saveUser(updated)
    }

    fun searchUserDTOById(userId: Long): UserResponseDTO {
        val user = searchUserById(userId)
        if (user.getIsPerson()) {
            return PersonResponseDTO(user as Person)
        } else {
            return InstitutionResponseDTO(user as Institution)
        }
    }

    fun deleteUserById(userId: Long) {
        val user = searchUserById(userId)
        petService.deletePetsByUsers(userId)
        user.active = false
        saveUser(user)
    }

    private fun searchUserById(userId: Long): User {
        return userRepository.findByIdAndActiveIsTrue(userId) ?: throw ElementNotFoundException()
    }

    private fun searchUserByMail(email: String): User {
        return userRepository.findByUserEmailAndActiveIsTrue(email) ?: throw ElementNotFoundException()
    }

    private fun alreadyRegistered(email: String): Boolean {
        return userRepository.existsByUserEmail(email)
    }

    private fun saveUser(user: User): User {
        return userRepository.save(user)
    }
}