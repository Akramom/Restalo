package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.*;
import static dev.morphia.query.experimental.filters.Filters.eq;

import ca.ulaval.glo2003.domain.entity.Owner;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Util;
import dev.morphia.Datastore;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantRepositoryMongo implements IRestaurantRepository {

  private Datastore datastore;

  public RestaurantRepositoryMongo(Datastore datastore) {
    this.datastore = datastore;
    this.datastore.getMapper().mapPackage(Restaurant.class.getPackageName());
    // this.datastore.getMapper().mapPackage("ca.ulaval.glo2003.domain.entity");
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

    System.out.println("on sauvegarde ici ");
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
}
