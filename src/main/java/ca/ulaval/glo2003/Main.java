package ca.ulaval.glo2003;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.errorMappers.ProcessingExceptionMapper;
import ca.ulaval.glo2003.errorMappers.RuntimeExceptionMapper;
import ca.ulaval.glo2003.resource.HealthResource;
import ca.ulaval.glo2003.resource.RestaurantResource;
import java.net.URI;
import java.time.LocalTime;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
  public static final String BASE_URI = "http://localhost:8080/";

  public static HttpServer startServer() {

    final ResourceConfig rc = new ResourceConfig();
    HealthResource healthCheckResource = new HealthResource();
    rc.register(healthCheckResource)
        .register(new RestaurantResource())
        .register(new RuntimeExceptionMapper())
        .register(new ProcessingExceptionMapper());
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  public static void main(String[] args) {

    startServer();

    System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
  }
}
