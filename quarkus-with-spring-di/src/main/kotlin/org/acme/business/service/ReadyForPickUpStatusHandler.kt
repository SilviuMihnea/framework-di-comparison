package org.acme.business.service

import org.acme.business.models.OrderStatus
import org.acme.business.models.Order

class ReadyForPickUpStatusHandler(
    private val notificationService: NotificationService
) : StatusHandler {
    override suspend fun canHandle(status: OrderStatus) = status == OrderStatus.READY_FOR_PICK_UP
    override val order: Int
        get() = 4

    override suspend fun handle(order: Order) {
        notificationService.notify("user", "order is ready for pick up; check the QR code in the application")
    }
}