## Koin

Let's say we have the following components: 

```kotlin
fun interface IdGenerator {
    fun generate(): UUID
}

class DefaultIdGenerator(): IdGenerator {
    override fun generate(): UUID {
        return UUID.randomUUID()
    }
}

class UserRepository(
    private val idGenerator: IdGenerator
) {
    fun createUser(name: String) {
        val id = idGenerator.generate()
        println("Creating $name with $id")
    }
}
```

## Configuring Koin

First, let's put all the real dependencies in a single module which we will override as needed.

```kotlin
fun Application.baseAppModule() = module {
    single<IdGenerator> { DefaultIdGenerator() }
    single<UserRepository> { UserRepository(get()) } // injecting in constructor
}
```

Then, we use that module in configuring Koin.

```kotlin
fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            baseAppModule()
        )
    }
}
```

## Injecting dependencies

```kotlin
fun Application.configureRouting() {
    val userRepository by inject<UserRepository>() // using inject
    routing {
        get("/") {
            val name = call.queryParameters["name"]
            if (name != null) {
                userRepository.createUser(name)
                call.respondText("Hello $name!")
            }
            call.respondText("Hello World!")
        }
    }
}
```

## Testing

For testing, we add a second module that adds overrides for some of the components. \\
Koin will use the overrides for instantiating the dependents. \\
We can keep the base app module to keep the dependencies between components.

```kotlin
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
                                    IdGenerator { 
                                        UUID.fromString("03403403-4034-453e-b564-193a706dbaa8") // stub
                                    } 
                                }
                            } 
                            // --- or --- 
                            module {
                                single<IdGenerator> {
                                    mockk {
                                        every { generate() } returns UUID.randomUUID()
                                    }
                                }
                            }
                        )
                    }
                }

                val response = client.get("/?name=John") // client is coming from testApplication
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldBe "Hello John!"
            }
        }
    }
)
```

## Closing components when application ends