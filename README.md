# Framework DI comparison

We'll have a look at the most popular options for dependency injection for Kotlin/Java frameworks:

- [Koin](./docs/Koin.md)
- [Dagger](./docs/Dagger.md)
- [Spring DI](./docs/SpringDI.md)

We have the following "architecture":

As a tech stack, I decided to keep as much libraries that we already use:

- KoTest
- Mockk
  
## Kotlin business code

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

## Comparison

|Koin|Dagger|SpringDI|
|:---:|:---:|:---:|
|Lightweight, no reflection|Requires code generation (KAPT/KSP)||
|Kotlin idiomatic|Annotation based|Annotation based|
|Can inject services only in Application|Uses @Component and @Module to group dependencies||
