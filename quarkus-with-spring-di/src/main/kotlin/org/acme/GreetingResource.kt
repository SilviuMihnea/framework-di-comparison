package org.acme

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.acme.business.service.NotificationService
import org.acme.business.service.StatusHandlerService
import org.acme.business.utility.IdGenerator
import org.springframework.beans.factory.annotation.Autowired

@Path("/hello")
class GreetingResource {

    @Autowired
    lateinit var idGenerator: IdGenerator

    @Autowired
    lateinit var statusHandlerService: StatusHandlerService

    @Autowired
    lateinit var notificationService: NotificationService

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        println(notificationService)
        println(statusHandlerService.handlers)
        return "Hello from Quarkus REST: ${idGenerator.generate() }"
    }
}