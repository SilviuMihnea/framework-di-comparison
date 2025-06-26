package com.example.business.service

import com.example.business.models.OrderStatus
import com.example.business.models.Order

interface StatusHandler {
    suspend fun handle(order: Order)

    suspend fun canHandle(status: OrderStatus): Boolean = false

    val order: Int
        get() = 5
  }