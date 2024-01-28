package ca.ulaval.glo2003;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;


@Path("healthcheck")
public class HealthCheckResource {

    @GET
    public Response healthCheck() {
        // Ici, vous pouvez ajouter votre logique de vérification
        // Par exemple, vérifier la connexion à la base de données, etc.
        boolean isHealthy = true; // Exemple de condition de santé

        if (isHealthy) {
            return Response.ok("Service is healthy").build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Service is unhealthy").build();
        }
    }
}
