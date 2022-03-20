package com.kotlin.petspace.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.kotlin.petspace.exceptions.ValidationException
import java.time.LocalDate
import javax.persistence.*

@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "user_type")
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
    var active: Boolean = true

    abstract fun getIsAdmin(): Boolean

    abstract fun getIsPerson(): Boolean

    abstract fun getFullName(): String

    fun validatePassword(password1: String, password2: String) {
        if (password1 != password2) {
            throw ValidationException("User or password incorrect!")
        }
    }

    open fun validateProfile() {
        if (name.isNullOrBlank()) {
            throw ValidationException("Name is mandatory")
        }

        if (birthDate == null) {
            throw ValidationException("Birthdate is mandatory")
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
    var admin: Boolean = false

    override fun getIsAdmin(): Boolean {
        return admin
    }

    override fun getIsPerson(): Boolean {
        return true
    }

    override fun getFullName(): String {
        return "$name $lastname"
    }

    override fun validateProfile() {
        super.validateProfile()
        if (lastname.isNullOrBlank()) {
            throw ValidationException("Lastname is mandatory")
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
