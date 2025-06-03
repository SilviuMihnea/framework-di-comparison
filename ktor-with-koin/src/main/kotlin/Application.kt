package com.example

import com.example.business.repo.OrderRepository
import com.example.business.repo.QRRepository
import com.example.business.utility.IdGenerator
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.getKoin
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.RequestScope

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureRouting()
}

fun Application.configureRouting() {
    val idGenerator by inject<IdGenerator>()
    routing {
        get("") {
            call.respondText("Hello ${idGenerator.generate()}!")
        }
        post("/order") {
            val scope = call.getKoin().createScope<RequestScope>()
            val orderRepository = scope.get<OrderRepository>()
            val qrRepository = scope.get<QRRepository>()

            // do something
            call.respondText("Hello World!")
        }
    }
}