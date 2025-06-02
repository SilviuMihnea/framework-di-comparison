package com.example

interface UserHandler {
    fun createUser(user: UserModel): Boolean
}

class UserHandlerImpl : UserHandler {
    override fun createUser(user: UserModel): Boolean {
        // Implementation logic (logging for now)
        return true
    }
}