package com.kotlin.petspace.repository

import com.kotlin.petspace.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUserEmailAndActiveIsTrue(userEmail: String): User?

    fun findByIdAndActiveIsTrue(userId: Long): User?

    fun existsByUserEmail(userEmail: String): Boolean

}