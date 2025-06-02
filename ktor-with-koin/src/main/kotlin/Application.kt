package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.util.UUID

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureRouting()
}

fun interface IdGenerator {
    fun generate(): UUID
}

class DefaultIdGenerator() : IdGenerator {
    override fun generate(): UUID {
        return UUID.randomUUID()
    }
}

class UserRepository(
    private val idGenerator: IdGenerator
) {
    fun createUser(name: String) {
        val id = idGenerator.generate()
        println("Creating $name with $id")
    }
}


fun Application.baseAppModule() = module {
    single<IdGenerator> { DefaultIdGenerator() }
    single<UserRepository> { UserRepository(get()) }
}

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            module {
                single<IdGenerator> { DefaultIdGenerator() }
                single<UserRepository> { UserRepository(get()) }
            }
        )
    }
}


fun Application.configureRouting() {
    val userRepository by inject<UserRepository>()
    routing {
        get("/") {
            val name = call.queryParameters["name"]
            if (name != null) {
                userRepository.createUser(name)
                call.respondText("Hello $name!")
            }
            call.respondText("Hello World!")
        }
    }
}