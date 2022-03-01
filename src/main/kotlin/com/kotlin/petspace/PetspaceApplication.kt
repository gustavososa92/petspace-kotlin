package com.kotlin.petspace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetspaceApplication

fun main(args: Array<String>) {
	runApplication<PetspaceApplication>(*args)
}
