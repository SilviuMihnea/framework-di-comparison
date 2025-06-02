import com.example.IdGenerator
import com.example.baseAppModule
import com.example.configureRouting
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.spec.style.Test
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import org.koin.ktor.plugin.Koin
import java.util.*

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
                            org.koin.dsl.module {
                                single<IdGenerator> {
                                    mockk {
                                        every { generate() } returns UUID.randomUUID()
                                    }
                                }
                            }
                        )
                    }
                }

                val response = client.get("/?name=John")
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldBe "Hello John!"
            }
        }
    }
)
