package org.acme.business.service

import org.acme.business.models.OrderStatus
import org.acme.business.models.Order

interface StatusHandler {
    suspend fun handle(order: Order)

    suspend fun canHandle(status: OrderStatus): Boolean = false

    val order: Int
        get() = 5
}