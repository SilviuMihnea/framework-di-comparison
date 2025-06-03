package org.acme


import org.acme.business.repo.*
import org.acme.business.service.*
import org.acme.business.utility.DefaultIdGenerator
import org.acme.business.utility.IdGenerator
import org.acme.business.utility.QR
import org.acme.business.utility.QRGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class DBConfig {
    @Bean
    @Scope(value = "request") // singleton / prototype / request / session / application / websocket
    fun dbConnection(): DBConnection {
        return DBConnection()
    }

    @Bean
    @Scope(value = "request")
    fun orderRepository(db: DBConnection): OrderRepository {
        return DefaultOrderRepository(db)
    }

    @Bean
    @Scope(value = "request")
    fun qrRepository(db: DBConnection): QRRepository {
        return DefaultQRRepository(db)
    }
}

@Configuration
class UtilityConfig {
    @Bean
    fun idGenerator(): IdGenerator {
        return DefaultIdGenerator()
    }

    @Bean
    fun qrGenerator(): QRGenerator {
        return QRGenerator { QR() }
    }
}

@Configuration
class NotificationConfig {
    @Bean
    fun notifier(): Notifier {
        return object : Notifier {
            // implement methods if needed
        }
    }
}

@Configuration
class HandlerConfig {
    @Bean(name = ["ReadyForPickUpStatusHandler"])
    fun readyForPickUpStatusHandler(notificationService: NotificationService): StatusHandler {
        return ReadyForPickUpStatusHandler(notificationService)
    }

    @Bean(name = ["WarehouseReadyStatusHandler"])
    fun warehouseReadyStatusHandler(notificationService: NotificationService): StatusHandler {
        return WarehouseReadyStatusHandler(notificationService)
    }

    @Bean
    fun statusHandlerService(
        @Qualifier("ReadyForPickUpStatusHandler") handler1: StatusHandler,
        @Qualifier("WarehouseReadyStatusHandler") handler2: StatusHandler
    ): StatusHandlerService {
        return StatusHandlerService(listOf(handler1, handler2))
    }
}
