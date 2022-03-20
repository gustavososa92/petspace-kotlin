package com.kotlin.petspace.controller

import com.kotlin.petspace.dto.UserLoginRequestDTO
import com.kotlin.petspace.dto.UserLoginResponseDTO
import com.kotlin.petspace.dto.UserRequestDTO
import com.kotlin.petspace.dto.UserResponseDTO
import com.kotlin.petspace.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    @Operation(description = "Requires mail and password for login")
    fun loginUser(
        @Valid @RequestBody userLogin: UserLoginRequestDTO
    ): UserLoginResponseDTO {
        return userService.loginUser(userLogin)
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Register a new user by type")
    fun registerPerson(
        @Valid @RequestBody newUser: UserRequestDTO
    ): UserLoginResponseDTO {
        return userService.registerUser(newUser)
    }

    @PutMapping("/{userId}/update")
    @Operation(description = "Updates an user")
    fun updatePerson(
        @Valid @RequestBody updatedUser: UserRequestDTO,
        @PathVariable userId: Long
    ) {
        userService.updateUser(userId, updatedUser)
    }

    @GetMapping("/{userId}/profile")
    @Operation(description = "Returns user info by id")
    fun getProfile(
        @PathVariable userId: Long
    ): UserResponseDTO {
        return userService.searchUserDTOById(userId)
    }

    @DeleteMapping("/{userId}/delete")
    @Operation(description = "Deletes an user by id")
    fun deleteUser(
        @PathVariable userId: Long
    ) {
        userService.deleteUserById(userId)
    }
}