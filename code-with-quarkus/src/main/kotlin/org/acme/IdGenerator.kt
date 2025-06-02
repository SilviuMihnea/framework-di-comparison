package org.acme

import org.springframework.stereotype.Component
import java.util.UUID

interface IdGenerator {
    fun generate(): UUID
}

@Component
class DefaultIdGenerator: IdGenerator {
    override fun generate(): UUID {
        return UUID.randomUUID()
    }
}