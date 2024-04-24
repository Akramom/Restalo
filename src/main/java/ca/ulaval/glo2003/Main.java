package ca.ulaval.glo2003;

import static ca.ulaval.glo2003.util.Constante.PORT;
import static ca.ulaval.glo2003.util.Constante.URL;

import ca.ulaval.glo2003.api.exceptionMapper.*;
import ca.ulaval.glo2003.api.resource.HealthResource;
import ca.ulaval.glo2003.api.resource.ReservationResource;
import ca.ulaval.glo2003.api.resource.RestaurantResource;
import ca.ulaval.glo2003.api.resource.SearchResource;
import ca.ulaval.glo2003.application.service.AvailabilityService;
import ca.ulaval.glo2003.application.service.ReservationService;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.util.DatastoreProvider;
import io.sentry.Sentry;
import java.net.URI;
import java.util.Optional;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
  public static String BASE_URI;

  private static IRestaurantRepository restaurantRepository;
  private static PersistenceType persistence;
  private static String mongoClusterUrl;
  private static String mongoDatabase;

  public static HttpServer startServer() {
    String port = System.getenv("PORT");
    if (port != null) {
      PORT = port;
    }
    BASE_URI = URL + ":" + PORT;

    Optional<String> entryPersistence = Optional.ofNullable(System.getProperty("persistence"));

    persistence =
        entryPersistence
            .map(PersistenceType::fromString)
            .orElseGet(() -> PersistenceType.fromString("inmemory"));
    System.out.println("persistence: " + persistence);

    if (persistence.equals(PersistenceType.MONGO)) {
      mongoClusterUrl = System.getenv("MONGO_CLUSTER_URL");
      mongoDatabase = System.getenv("MONGO_DATABASE");
      restaurantRepository =
          new RestaurantRepositoryMongo(
              new DatastoreProvider(mongoClusterUrl, mongoDatabase).provide());
    } else restaurantRepository = new RestaurantRepositoryInMemory();

    //DSN: https://2394ba6d463c6084cdcf709801c55847@o4507098785972224.ingest.de.sentry.io/4507098849738832
    Sentry.init(
        options -> {
          options.setDsn(System.getenv("SENTRY_DSN"));

          options.setTracesSampleRate(1.0);
        });

    final ResourceConfig rc = new ResourceConfig();
    HealthResource healthCheckResource = new HealthResource();
    AvailabilityService availabilityService = new AvailabilityService(restaurantRepository);
    RestaurantService restaurantService = new RestaurantService(restaurantRepository);
    ReservationService reservationService = new ReservationService(restaurantRepository);
    reservationService.setAvailabilityService(availabilityService);
    restaurantService.setReservationService(reservationService);
    restaurantService.setAvailabilityService(availabilityService);
    rc.register(healthCheckResource)
        .register(new RestaurantResource(restaurantService))
        .register(new ReservationResource(reservationService))
        .register(new SearchResource(restaurantService))
        .register(new RuntimeExceptionMapper())
        .register(new ProcessingExceptionMapper())
        .register(new InvalidParameterExceptionMapper())
        .register(new MissingParameterExceptionMapper())
        .register(new NotFoundExceptionMapper())
        .register(new DateTimeExceptionMapper());
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  public static void main(String[] args) {

    startServer();

    System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
  }
}
