package ca.ulaval.glo2003.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import java.util.concurrent.TimeUnit;

public class DatastoreProvider {

  private final String mongoClusterUrl;
  private final String mongoDatabase;
  private Datastore datastore;

  public DatastoreProvider(String mongoClusterUrl, String mongoDatabase) {
    this.mongoClusterUrl = mongoClusterUrl;
    this.mongoDatabase = mongoDatabase;
  }

  public Datastore provide() {
    try {
      MongoClientSettings settings =
          MongoClientSettings.builder()
              .applyToClusterSettings(
                  builder -> builder.serverSelectionTimeout(5000, TimeUnit.MILLISECONDS))
              .applyToSocketSettings(builder -> builder.connectTimeout(500, TimeUnit.MILLISECONDS))
              .applyToConnectionPoolSettings(
                  builder -> builder.maxConnectionIdleTime(1000, TimeUnit.MILLISECONDS))
              .applyConnectionString(new ConnectionString(mongoClusterUrl))
              .build();

      datastore =
          Morphia.createDatastore(
              MongoClients.create(settings), mongoDatabase == null ? "restalo" : mongoDatabase);
      System.out.println(datastore.getDatabase().getName());
      System.out.println("Connexion à base de donnée réussie.");
    } catch (Exception e) {
      throw new RuntimeException(
          "Erreur lors de la connexion à base de donnée  : " + e.getMessage());
    }

    return datastore;
  }
}
