package com.kotlin.petspace.dto

import com.kotlin.petspace.model.Institution
import com.kotlin.petspace.model.Person
import com.kotlin.petspace.model.User
import java.time.LocalDate

abstract class UserResponseDTO(user: User) {
    var userId: Long = -999
    var name: String
    var userEmail: String
    var address: String
    var birthDate: LocalDate
    var phoneNumber: String
    abstract var type: String
    abstract var isPerson: Boolean

    init {
        userId = user.id!!
        name = user.name!!
        userEmail = user.userEmail!!
        address = user.address!!
        birthDate = user.birthDate!!
        phoneNumber = user.phoneNumber!!
    }
}

class PersonResponseDTO(user: Person) : UserResponseDTO(user as User) {
    //    override lateinit var name: String
//    override lateinit var userEmail: String
//    override lateinit var address: String
//    override lateinit var birthDate: LocalDate
//    override lateinit var phoneNumber: String
    override var type: String = "Person"
    override var isPerson: Boolean = true
    var lastname: String
    var gender: String

    init {
//        userId = user.id
//        name = user.name
//        userEmail = user.userEmail
//        address = user.address
//        birthDate = user.birthDate
//        phoneNumber = user.phoneNumber
        lastname = user.lastname!!
        gender = user.gender!!
    }

}


open class InstitutionResponseDTO(user: Institution) : UserResponseDTO(user as User) {
    //    override lateinit var name: String
//    override lateinit var userEmail: String
//    override lateinit var address: String
//    override lateinit var birthDate: LocalDate
//    override lateinit var phoneNumber: String
    override var type: String = "Institution"
    override var isPerson: Boolean = false
    var description: String


    init {
//        userId = user.id
//        name = user.name
//        userEmail = user.userEmail
//        address = user.address
//        birthDate = user.birthDate
//        phoneNumber = user.phoneNumber
        description = user.description!!
    }


}

class UserBasicResponseDTO(val userId: Long, val fullName: String)