import com.example.*
import com.example.business.utility.IdGenerator
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
import org.koin.dsl.module
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
                            dbModule,
                            utilityModule,
                            notificationModule,
                            handlerModule,
                            module {
                                single<IdGenerator> {
                                    mockk {
                                        every { generate() } returns UUID.fromString("11e1e111-e111-1e11-ee11-1e11e11e11ee")
                                    }
                                }
                            }
                        )
                    }
                }

                val response = client.get("/")
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldBe "Hello 11e1e111-e111-1e11-ee11-1e11e11e11ee!"
            }
        }
    }
)
