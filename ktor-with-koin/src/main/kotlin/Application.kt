package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureRouting()
}


fun Application.baseAppModule() = module {
    single<IdGenerator> { DefaultIdGenerator() }
    single<UserRepository> { UserRepository(get()) }
}

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            baseAppModule()
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