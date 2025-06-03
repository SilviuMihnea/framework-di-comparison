package com.example.business.utility

import java.util.*

fun interface IdGenerator {
    fun generate(): UUID
}

class DefaultIdGenerator() : IdGenerator {
    override fun generate(): UUID {
        return UUID.randomUUID()
    }
}