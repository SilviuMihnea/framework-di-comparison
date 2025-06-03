package com.example

import com.example.business.repo.*
import com.example.business.service.*
import com.example.business.utility.DefaultIdGenerator
import com.example.business.utility.IdGenerator
import com.example.business.utility.QR
import com.example.business.utility.QRGenerator
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

class RequestScope
class TenantScope // different http clients or token providers

val dbModule = module {
    scope<RequestScope> {
        factory<DBConnection> { DBConnection() }
        factory<OrderRepository> { DefaultOrderRepository(get()) }
        factory<QRRepository> { DefaultQRRepository(get()) }
    }
}

val utilityModule = module {
    single<IdGenerator> { DefaultIdGenerator() }
    single<QRGenerator> { QRGenerator { QR() } }
}

val notificationModule = module {
    single<Notifier> { object : Notifier {} }
    singleOf(::DefaultNotificationService)
}

val handlerModule = module {
    single<StatusHandler>(named("ReadyForPickUpStatusHandler")) { ReadyForPickUpStatusHandler(get()) }
    single<StatusHandler>(named("WarehouseReadyStatusHandler")) { WarehouseReadyStatusHandler(get()) }
    single<StatusHandlerService> {
        StatusHandlerService(
            listOf( // you need to create the list manually, koin does not group dependencies
                get(named("ReadyForPickUpStatusHandler")),
                get(named("WarehouseReadyStatusHandler")),
            )
        )
    }

    // other use cases: auth http client and no-auth http client or different http clients bor different services
}


fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            dbModule,
            utilityModule,
            notificationModule,
            handlerModule
        )
    }
}
