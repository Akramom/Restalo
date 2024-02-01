package ca.ulaval.glo2003.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/health")
public class HealthResource {

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response healthCheck() {
        return Response.ok("Service is healthy").build();
    }
}
