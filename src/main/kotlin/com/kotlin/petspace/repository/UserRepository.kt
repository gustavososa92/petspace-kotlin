package com.kotlin.petspace.repository

import com.kotlin.petspace.model.User
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUserEmailAndActiveIsTrue(userEmail: String): Optional<User>

    fun findByIdAndActiveIsTrue(userId: Long): Optional<User>

    fun existsByUserEmail(userEmail: String): Boolean

}