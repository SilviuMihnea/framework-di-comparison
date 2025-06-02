package org.acme

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import org.springframework.beans.factory.annotation.Autowired

@Path("/hello")
class GreetingResource {

    @Autowired
    lateinit var idGenerator: IdGenerator

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(@QueryParam("name") name: String? = "world"): String {
        idGenerator.generate().also {
            println(it)
        }
        return "Hello from Quarkus REST"
    }
}