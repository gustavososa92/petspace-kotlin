package com.kotlin.petspace.controller

import com.kotlin.petspace.dto.UserLoginRequestDTO
import com.kotlin.petspace.dto.UserLoginResponseDTO
import com.kotlin.petspace.dto.UserRequestDTO
import com.kotlin.petspace.dto.UserResponseDTO
import com.kotlin.petspace.service.UserService
import io.swagger.v3.oas.annotations.Operation
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    @Operation(description = "Realiza el chequeo para el ingreso a la aplicación. Requiere mail y password.")
    fun loginUser(
        @Valid @RequestBody userLogin: UserLoginRequestDTO
    ): UserLoginResponseDTO {
        return userService.loginUser(userLogin)
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Registra un nuevo usuario del tipo")
    fun registerPerson(
        @Valid @RequestBody newUser: UserRequestDTO
    ): UserLoginResponseDTO {
        return userService.registerUser(newUser)
    }

    @PutMapping("/{userId}/update")
    @Operation(description = "Actualiza un usuario")
    fun updatePerson(
        @Valid @RequestBody updatedUser: UserRequestDTO,
        @PathVariable userId: Long
    ): UserResponseDTO {
        return userService.updateUser(userId, updatedUser)
    }

    @GetMapping("/{userId}/profile")
    @Operation(description = "Devuelve el usuario para armar el perfil.")
    fun getProfile(
        @PathVariable userId: Long
    ): UserResponseDTO {
        return userService.searchUserById(userId)
    }

    @DeleteMapping("/{userId}/delete")
    @Operation(description = "Devuelve el usuario para armar el perfil.")
    fun deleteUser(
        @PathVariable userId: Long
    ): Long {
        userService.deleteUserById(userId)
        return userId
    }
}