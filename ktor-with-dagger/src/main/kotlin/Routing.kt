package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val idGenerator = DaggerAppScopeComponent.create().idGenerator()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/order") {
            post {
                val scopeComponent = DaggerRequestScopeComponent.create()
                val orderRepository = scopeComponent.orderRepository()
                val qrRepository = scopeComponent.qrRepository()
                call.respondText("Hello World!")
            }
        }
    }
}

