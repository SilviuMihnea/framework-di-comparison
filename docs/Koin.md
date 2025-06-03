# Koin

This example demonstrates how to use **Koin** for dependency injection in a Kotlin-Ktor project with other libraries we already use:

- KoTest
- Mockk

## Setup

```kotlin
dependencies {
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    testImplementation(libs.koin.test)
    // ... other deps
}

```
  
## Defining components

Following interface based style, we will declare an interface than an implementation

```kotlin
interface NotificationService {
    suspend fun notify(topic: String, message: String)
}

interface Notifier // some dependency for the pur implementation

class DefaultNotificationService(
    val notifier: Notifier
) : NotificationService {
    override suspend fun notify(topic: String, message: String) {
        // empty
    }
}
```

Then, we create a Koin module.

```kotlin
val notificationModule = module {
    single<Notifier> { object : Notifier {} }.onClose { // do something to close it } // singleton
    singleOf(::DefaultNotificationService) // easier than DefaultNotificationService(get())
}
```

And for scoped components, like per-request or per-session:

```kotlin
val dbModule = module {
    scope<RequestScope> {
        factory<DBConnection> { DBConnection() }.onClose { //do something to close it }
        factory<OrderRepository> { DefaultOrderRepository(get()) }
        factory<QRRepository> { DefaultQRRepository(get()) }
    }
}
```

Since Koin is using lambdas, it is extremely easy to replace a component in tests

## Injecting dependencies

This is how we inject singletons:

```kotlin
val userRepository by inject<UserRepository>() // using inject in app
```

And this is how we use scopes:

```kotlin
val scope = call.getKoin().createScope<RequestScope>()
val orderRepository = scope.get<OrderRepository>()
val qrRepository = scope.get<QRRepository>()```
```

There is no support for injecting dependencies in classes. We have to specifically inject them in extension  methods of the Ktor `Application` class.

## Testing

For testing, we add a second module that adds overrides for some of the components. \\
Koin will use the overrides for instantiating the dependents. \\
We can keep the base app module to keep the dependencies between components.

```kotlin
fun Application.configureRouting() {
    val idGenerator by inject<IdGenerator>()
    routing {
        get("") {
            call.respondText("Hello ${idGenerator.generate()}!")
        }
    }
}

@Test
class ApplicationTest : StringSpec(
    {
        "should make a request to the app " {
            testApplication {
                application {
                    configureRouting()
                    install(Koin) {
                        modules(
                            baseAppModule(),
                            module {
                                single<IdGenerator> { 
                                    IdGenerator { // fun interface stub
                                        UUID.fromString("03403403-4034-453e-b564-193a706dbaa8") 
                                    } 
                                }
                            } 
                            // --- or --- 
                            module {
                                single<IdGenerator> {
                                    mockk {
                                        every { generate() } 
                                        returns UUID.fromString("03403403-4034-453e-b564-193a706dbaa8") 
                                    }
                                }
                            }
                        )
                    }
                }

                val response = client.get("") // client is coming from testApplication
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldBe "Hello 03403403-4034-453e-b564-193a706dbaa8!"
            }
        }
    }
)
```

## Single, Factory, Scoped

```kotlin
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

val compositeModule = module {
    includes(notificationModule, handlerModule)
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
```
