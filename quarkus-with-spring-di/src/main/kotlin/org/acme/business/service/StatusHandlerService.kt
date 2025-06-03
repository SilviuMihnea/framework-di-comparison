package org.acme.business.service

import org.acme.business.models.Order

class StatusHandlerService(
    val handlers: List<StatusHandler>
) {
    suspend fun handle(order: Order) {
        handlers.filter { it.canHandle(order.status) }
            .sortedBy { it.order }
            .firstOrNull()?.handle(order)
    }
}