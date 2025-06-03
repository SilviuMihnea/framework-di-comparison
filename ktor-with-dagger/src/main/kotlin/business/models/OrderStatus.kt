package com.example.business.models

enum class OrderStatus {
    WAREHOUSE_READY,
    DELIVERY_STARTED,
    DELIVERY_PROBLEM,
    DELIVERY_SUCCEEDED,
    READY_FOR_PICK_UP,
    PICKED_UP,
    SCHEDULED_FOR_RETURN
}