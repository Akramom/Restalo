package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.util.DatastoreProvider;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class RestaurantRepositoryMongoTest extends AbstractRestaurantRepositoryTest {
  private DatastoreProvider datastoreProvider;

  @Container
  private final MongoDBContainer mongoDBContainer =
      new MongoDBContainer(DockerImageName.parse("mongo:7.0"));

  ;

  @Override
  RestaurantRepository createPersistence() {
    datastoreProvider =
        new DatastoreProvider(mongoDBContainer.getConnectionString(), "restaloTest");
    return new RestaurantRepositoryMongo(datastoreProvider.provide());
  }
}
