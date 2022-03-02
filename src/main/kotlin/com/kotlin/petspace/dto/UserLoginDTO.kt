package com.kotlin.petspace.dto

import com.kotlin.petspace.model.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserLoginResponseDTO(val userId: Long, val isPerson: Boolean, val isAdmin: Boolean) {
    companion object {
        fun toDTO(user: User): UserLoginResponseDTO {
            return UserLoginResponseDTO(user.id!!, user.getIsPerson(), user.getIsAdmin())
        }
    }
}

data class UserLoginRequestDTO(
    @Email(message = "Debe ingresar un mail válido")
    var userEmail: String,

    @NotBlank(message = "Debe ingresar la contraseña")
    var password: String
)