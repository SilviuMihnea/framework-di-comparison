package com.example.business.service

interface NotificationService {
    suspend fun notify(topic: String, message: String)
}

interface Notifier

class DefaultNotificationService(
    val notifier: Notifier
) : NotificationService {
    override suspend fun notify(topic: String, message: String) {
        // empty
    }
}