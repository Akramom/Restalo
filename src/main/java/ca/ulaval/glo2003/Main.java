package ca.ulaval.glo2003;

import ca.ulaval.glo2003.api.exceptionMapper.*;
import ca.ulaval.glo2003.api.resource.HealthResource;
import ca.ulaval.glo2003.api.resource.ReservationResource;
import ca.ulaval.glo2003.api.resource.RestaurantResource;
import ca.ulaval.glo2003.api.resource.SearchResource;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.repository.*;
import java.net.URI;
import java.util.Optional;

import ca.ulaval.glo2003.util.DatastoreProvide;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
  public static String BASE_URI;

  private static RestaurantRepository restaurantRespository;
  private static PersistenceType persistence;
  private static String mongoClusterUrl;
  private static String mongoDatabase;

  public static HttpServer startServer() {

    String port = System.getenv("PORT");
    if (port == null) {
      port = "8080";
    }
    BASE_URI = "http://0.0.0.0:" + port;

    Optional<String> entryPersistence = Optional.ofNullable(System.getProperty("persistence"));

    persistence =
        entryPersistence
            .map(PersistenceType::fromString)
            .orElseGet(() -> PersistenceType.fromString("inmemory"));
    System.out.println("persistence: " + persistence);


    final ResourceConfig rc = new ResourceConfig();
    HealthResource healthCheckResource = new HealthResource();
    RestaurantRepository restaurantRespository = new RestaurantRepository();
    RestaurantService restaurantService = new RestaurantService(restaurantRespository);
    rc.register(healthCheckResource)
        .register(new RestaurantResource(restaurantService))
        .register(new ReservationResource(restaurantService))
        .register(new SearchResource(restaurantService))
        .register(new RuntimeExceptionMapper())
        .register(new ProcessingExceptionMapper())
        .register(new InvalidParameterExceptionMapper())
        .register(new MissingParameterExceptionMapper())
        .register(new NotFoundExceptionMapper());
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  public static void main(String[] args) {

    startServer();

    System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
  }
}
