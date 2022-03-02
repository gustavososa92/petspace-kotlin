package com.kotlin.petspace.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType

@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = Person::class, name = "Person"),
        JsonSubTypes.Type(value = Institution::class, name = "Institution")
    ]
)
@Inheritance(strategy = InheritanceType.JOINED)
abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(length = 150)
    var name: String? = null

    @Column(length = 150)
    var userEmail: String? = null

    @Column(length = 150)
    var password: String? = null

    @Column(length = 150)
    var address: String? = null

    @Column(length = 150)
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    var birthDate: LocalDate? = null

    @Column(length = 150)
    var phoneNumber: String? = null

    @Column
    var isActive: Boolean = true

    abstract fun getIsAdmin(): Boolean

    abstract fun getIsPerson(): Boolean

    abstract fun getFullName(): String

    open fun merge(updatedUser: User) {
        name = updatedUser.name ?: name
        userEmail = updatedUser.userEmail ?: userEmail
        password = updatedUser.password ?: password
        address = updatedUser.address ?: address
        birthDate = updatedUser.birthDate ?: birthDate
        phoneNumber = updatedUser.phoneNumber ?: phoneNumber
    }

    fun validatePassword(password1: String, password2: String) {
        if (password1 != password2) {
            throw RuntimeException("Usuario o password incorrecta!")
        }
    }

    open fun validateProfile() {
        if (name.isNullOrBlank()) {
            throw RuntimeException("Debe cargar el nombre")
        }

        if (birthDate == null) {
            throw RuntimeException("Debe cargar una fecha de nacimiento")
        }
    }


}

@Entity
class Person : User() {

    @Column(length = 150)
    var lastname: String? = null

    @Column(length = 150)
    var gender: String? = null

    @Column
    var isAdmin: Boolean = false

    override fun getIsAdmin(): Boolean {
        return isAdmin
    }

    override fun getIsPerson(): Boolean {
        return true
    }

    override fun getFullName(): String {
        return "$name $lastname"
    }

    override fun merge(updatedUser: User) {
        super.merge(updatedUser)
        updatedUser as Person
        gender = updatedUser.gender ?: gender
        lastname = updatedUser.lastname ?: lastname
    }

    override fun validateProfile() {
        super.validateProfile()
        if (lastname.isNullOrBlank()) {
            throw RuntimeException("Debe cargar el apellido")
        }
    }

}

@Entity
class Institution : User() {

    @Column(length = 150)
    var description: String? = null

    override fun getIsAdmin(): Boolean {
        return false
    }

    override fun getIsPerson(): Boolean {
        return false
    }

    override fun getFullName(): String {
        return name ?: "Error getting Full Name"
    }

}