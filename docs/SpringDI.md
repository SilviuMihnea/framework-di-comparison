# Spring DI

This example demonstrates how to use **Spring DI** for dependency injection in a Kotlin-Quarkus project.

## Setup

After creating a project with <https://code.quarkus.io/>, you can add in the CLI:

```
quarkus extension add spring-di
```
  
## Defining components

Following interface based style, we will declare an interface than an implementation

```kotlin
interface NotificationService {
    suspend fun notify(topic: String, message: String)
}

interface Notifier // some dependency for the implementation

@Service // <-- Spring specific
class DefaultNotificationService(
    val notifier: Notifier
) : NotificationService {
    override suspend fun notify(topic: String, message: String) {
        // empty
    }
}
```

As usual, in Spring, we don't need to create any additional modules or components.

In case we don't have access to the said class we wish for a component, we can create a configuration class:

```kotlin
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
```

## Injecting dependencies

The injection method is the Spring default one. Since we work with kotlin which insists on type safety, if we want to use @Autowired on a propriety we have to declare it as lateinit var:

```kotlin
@Autowired
lateinit var notificationService: NotificationService
```

Which in turn has to be checked every time we want to use a method.

## Testing

For testing, it's the easiest from all of them, since we can just use @InjectMock:

```kotlin
@QuarkusTest
class GreetingResourceTest {

    @InjectMock
    lateinit var notificationService: NotificationService

    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/hello")
          .then()
             .statusCode(200)
             .body(`is`("Hello from Quarkus REST"))
    }

}
```

## Single, Factory, Scoped

```kotlin
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

```
