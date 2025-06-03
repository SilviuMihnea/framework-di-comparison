package org.acme.business.service

import org.acme.business.models.OrderStatus
import org.acme.business.models.Order

class WarehouseReadyStatusHandler(
    private val notificationService: NotificationService
) : StatusHandler {
    override suspend fun canHandle(status: OrderStatus) = status == OrderStatus.WAREHOUSE_READY
    override val order: Int
        get() = 0

    override suspend fun handle(order: Order) {
        notificationService.notify("user", "order is completed in the warehouse")
    }
}