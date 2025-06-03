package org.acme

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.acme.business.service.NotificationService
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

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