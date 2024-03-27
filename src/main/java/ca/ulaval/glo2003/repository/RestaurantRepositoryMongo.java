package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.*;
import static dev.morphia.query.experimental.filters.Filters.eq;

import ca.ulaval.glo2003.domain.entity.Owner;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Util;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantRepositoryMongo implements IRestaurantRepository {

  private Datastore datastore;

  public RestaurantRepositoryMongo(Datastore datastore) {
    this.datastore = datastore;
    this.datastore.getMapper().mapPackage(Restaurant.class.getPackageName());
  }

  @Override
  public List<Owner> getOwners() {
    return datastore.find(Owner.class).stream().toList();
  }

  @Override
  public Owner getOwner(String ownerId) throws NotFoundException {
    return datastore.find(Owner.class).filter(eq("ownerId", ownerId)).stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException(OWNER_NOT_FOUND));
  }

  @Override
  public Restaurant addRestaurant(String ownerId, Restaurant restaurant) throws NotFoundException {

    if (restaurant.getId() == null) {
      restaurant.setId(Util.generateId());
    }

    Owner owner = getOwner(ownerId);
    restaurant.setOwnerId(owner.getOwnerId());
    return datastore.save(restaurant);
  }

  @Override
  public Owner addOwner(String ownerId) {
    return datastore.save(new Owner(ownerId, "Doe", "John", "418-"));
  }

  @Override
  public Restaurant getOwnerRestaurantById(String ownerId, String restaurantId)
      throws NotFoundException {
    return datastore
        .find(Restaurant.class)
        .filter(eq("ownerId", ownerId), eq("id", restaurantId))
        .stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND));
  }

  @Override
  public Restaurant getRestaurantById(String restaurantId) throws NotFoundException {
    return datastore.find(Restaurant.class).filter(eq("id", restaurantId)).stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND));
  }

  @Override
  public List<Restaurant> getAllOwnerRestaurants(String ownerId) throws NotFoundException {
    return datastore.find(Restaurant.class).filter(eq("ownerId", ownerId)).stream()
        .collect(Collectors.toList());
  }

  @Override
  public List<Restaurant> getAllRestaurants() {
    return datastore.find(Restaurant.class).stream().collect(Collectors.toList());
  }

  @Override
  public Reservation addReservation(Reservation reservation, String restaurantId)
      throws NotFoundException {
    if (reservation.getNumber() == null) reservation.setNumber(Util.generateId());
    Restaurant restaurant = getRestaurantById(restaurantId);
    reservation.setRestaurantId(restaurant.getId());
    return datastore.save(reservation);
  }

  @Override
  public Reservation getReservationByNumber(String reservationNumer) throws NotFoundException {
    return datastore.find(Reservation.class).filter(eq("number", reservationNumer)).stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND));
  }

  @Override
  public Restaurant getRestaurantByReservationNumber(String number) throws NotFoundException {
    Reservation reservation = getReservationByNumber(number);
    return getRestaurantById(reservation.getRestaurantId());
  }

  @Override
  public void deleteOwnerRestaurantById(String ownerId, String restaurantId)
      throws NotFoundException {
    getOwnerRestaurantById(ownerId, restaurantId);

    datastore
        .find(Restaurant.class)
        .filter(eq("id", restaurantId))
        .delete(new DeleteOptions().multi(false));

    datastore
        .find(Reservation.class)
        .filter(eq("restaurantId", restaurantId))
        .delete(new DeleteOptions().multi(true));
    /*

    	datastore.delete(Restaurant.class, restaurantId);

    datastore.delete(restaurant, new DeleteOptions());

    datastore.find(Restaurant.class)
    			.filter(Filters.eq("id", restaurantId))
    			.delete(new DeleteOptions());

    	datastore.delete(datastore.find(Restaurant.class)
    			.filter(Filters.eq("id", restaurantId)).stream().findFirst().orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND)));

    	datastore.find(Reservation.class)
    			.filter(Filters.eq("restaurantId", restaurantId))
    			.delete(new DeleteOptions().multi(true));

    	Bson studentFilter = Filters.eq( "id", doc.get("id") );
    	Bson delete = Updates.pull("scores", new Document("score", lowestHomework).append("type", "homework"));
    	collection.updateOne(studentFilter, delete);

    	getOwnerRestaurantById(ownerId, restaurantId);

    	datastore.update(pull("restaurants", eq("id", restaurantId)));

    	datastore
    			.find(Owner.class)
    			.filter(eq("ownerId", ownerId)).pull("restaurants", eq("id", restaurantId));

    	List<Restaurant> restaurants = datastore.find(Restaurant.class).filter(eq("ownerId", ownerId), ne("id", restaurantId)).stream().toList();
    	System.out.println(restaurants);
    	datastore.delete("id", restaurantId);
    	*/
  }
}
