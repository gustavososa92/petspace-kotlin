package com.kotlin.petspace.service

import org.springframework.stereotype.Service

@Service
class PetService {

    fun deletePetsByUsers(userId: Long) {
        println("deleted pets of: userId")
    }

}