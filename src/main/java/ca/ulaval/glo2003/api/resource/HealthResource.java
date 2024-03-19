package ca.ulaval.glo2003.api.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/health")
public class HealthResource {

  public final String SERVICE_IS_HEALTHY = "Service is healthy";

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public Response healthCheck() {
    return Response.ok(SERVICE_IS_HEALTHY).build();
  }
}
