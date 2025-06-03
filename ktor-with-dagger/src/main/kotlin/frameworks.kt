package com.example

import com.example.business.repo.*
import com.example.business.service.*
import com.example.business.utility.DefaultIdGenerator
import com.example.business.utility.IdGenerator
import com.example.business.utility.QR
import com.example.business.utility.QRGenerator
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReadyForPickUp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WarehouseReady

@Module
class DBModule {
    @Provides
    fun dbConnection(): DBConnection {
        return DBConnection()
    }

    @Provides
    fun orderRepository(db: DBConnection): OrderRepository {
        return DefaultOrderRepository(db)
    }

    @Provides
    fun qrRepository(db: DBConnection): QRRepository {
        return DefaultQRRepository(db)
    }
}

@Module
class UtilityModule {
    @Provides
    fun idGenerator(): IdGenerator {
        return DefaultIdGenerator()
    }

    @Provides
    fun qrGenerator(): QRGenerator {
        return QRGenerator { QR() }
    }
}

@Module
class NotificationModule {
    @Provides
    fun notifier(): Notifier {
        return object : Notifier {
            // implement methods if needed
        }
    }

    @Provides
    fun notificationService(notifier: Notifier): NotificationService {
        return DefaultNotificationService(notifier)
    }
}

@Module
class HandlerModule {
    @Provides
    @ReadyForPickUp
    fun readyForPickUpStatusHandler(notificationService: NotificationService): StatusHandler {
        return ReadyForPickUpStatusHandler(notificationService)
    }

    @Provides
    @WarehouseReady
    fun warehouseReadyStatusHandler(notificationService: NotificationService): StatusHandler {
        return WarehouseReadyStatusHandler(notificationService)
    }

    @Provides
    fun statusHandlerService(
        @ReadyForPickUp handler1: StatusHandler,
        @WarehouseReady handler2: StatusHandler
    ): StatusHandlerService {
        return StatusHandlerService(listOf(handler1, handler2))
    }
}


@Singleton
@Component(
    modules = [
        DBModule::class,
    ]
)
interface RequestScopeComponent {
    fun orderRepository(): OrderRepository
    fun qrRepository(): QRRepository
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestScope

@RequestScope
@Component(
    modules = [
        NotificationModule::class,
        HandlerModule::class,
        UtilityModule::class
    ]
)
interface AppScopeComponent {
    fun idGenerator(): IdGenerator
    fun qrGenerator(): QRGenerator
    fun statusHandlerService(): StatusHandlerService
}
