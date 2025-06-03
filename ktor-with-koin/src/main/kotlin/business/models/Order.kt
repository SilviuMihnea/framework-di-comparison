package com.example.business.models

import java.util.*

data class Order(
    val id: UUID,
    val items: List<OrderItem>,
    val status: OrderStatus
)

data class OrderItem(
    val orderId: UUID,
    val itemId: UUID,
    val quantity: Int
)