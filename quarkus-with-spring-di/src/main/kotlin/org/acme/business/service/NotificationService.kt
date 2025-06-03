package org.acme.business.service

import org.springframework.stereotype.Service

interface NotificationService {
    suspend fun notify(topic: String, message: String)
}

interface Notifier

@Service
class DefaultNotificationService(
    val notifier: Notifier
) : NotificationService {
    override suspend fun notify(topic: String, message: String) {
        // empty
    }
}