package com.example

class UserRepository(
    private val idGenerator: IdGenerator
) {
    fun createUser(name: String) {
        val id = idGenerator.generate()
        println("Creating $name with $id")
    }
}