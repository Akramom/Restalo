package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.*;
import static dev.morphia.query.experimental.filters.Filters.eq;
import static dev.morphia.query.experimental.filters.Filters.gte;

import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.entity.Owner;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Util;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.experimental.updates.UpdateOperators;
import java.time.LocalDate;
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
    return datastore.save(new Owner(ownerId));
  }

  @Override
  public Restaurant getOwnerRestaurantById(String ownerId, String restaurantId)
      throws NotFoundException {
    getOwner(ownerId);
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
  public void deleteRestaurantIfOwner(String restaurantId, String ownerId)
      throws NotFoundException {
    getOwnerRestaurantById(ownerId, restaurantId);

    deleteRestaurantReservations(restaurantId);
    deleteRestaurant(restaurantId);
  }

  public void deleteRestaurantReservations(String restaurantId) {
    datastore
        .find(Reservation.class)
        .filter(eq("restaurantId", restaurantId))
        .delete(new DeleteOptions().multi(true));
  }

  public void deleteRestaurant(String restaurantId) {
    datastore
        .find(Restaurant.class)
        .filter(eq("id", restaurantId))
        .delete(new DeleteOptions().multi(false));
  }

  @Override
  public boolean isExistAvailabilityForADate(String restaurantId, LocalDate date)
      throws NotFoundException {
    return !getAvailabilitiesForADate(restaurantId, date).isEmpty();
  }

  @Override
  public List<Availability> getAvailabilitiesForADate(String restaurantId, LocalDate date)
      throws NotFoundException {
    Restaurant restaurant = getRestaurantById(restaurantId);

    return datastore
        .find(Availability.class)
        .filter(eq("restaurantId", restaurant.getId()))
        .stream()
        .filter(availability -> availability.getStart().toLocalDate().equals(date))
        .toList();
  }

  @Override
  public void addAvailabilitiesForADate(String restaurantId, LocalDate date)
      throws NotFoundException {
    Restaurant restaurant = getRestaurantById(restaurantId);
    restaurant.addAvailabilities(date);
    datastore.save(restaurant.getAvailabilities());
  }

  @Override
  public void updateAvailability(Availability updatedAvailability) {
    datastore
        .find(Availability.class)
        .filter(eq("id", updatedAvailability.getId()))
        .modify(UpdateOperators.set("remainingPlaces", updatedAvailability.getRemainingPlaces()))
        .execute();
  }

  @Override
  public void deleteReservation(String reservationNumber, String restaurantId) {
    datastore.find(Reservation.class).filter(eq("number", reservationNumber)).delete();
  }

  @Override
  public List<Reservation> getReservationsByRestaurantId(String ownerId, String restaurantId)
      throws NotFoundException {
    getOwner(ownerId);
    getOwnerRestaurantById(ownerId, restaurantId);
    return datastore.find(Reservation.class).filter(eq("restaurantId", restaurantId)).stream()
        .toList();
  }

  @Override
  public void updateReservation(Reservation updatedReservation) {
    datastore
        .find(Reservation.class)
        .filter(eq("number", updatedReservation.getNumber()))
        .modify(
            UpdateOperators.set("date", updatedReservation.getDate()),
            UpdateOperators.set("startTime", updatedReservation.getStartTime()),
            UpdateOperators.set("endTime", updatedReservation.getEndTime()),
            UpdateOperators.set("groupSize", updatedReservation.getGroupSize()))
        .execute();
  }

  @Override
  public void updateRestaurant(Restaurant updatedRestaurant) {

    datastore
            .find(Restaurant.class)
            .filter(eq("id", updatedRestaurant.getId()))
            .modify(
                    UpdateOperators.set("hours", updatedRestaurant.getHours()),
                    UpdateOperators.set("capacity", updatedRestaurant.getCapacity()),
                    UpdateOperators.set("name", updatedRestaurant.getName()),
                    UpdateOperators.set("reservationDuration", updatedRestaurant.getReservation()))
            .execute();
  }

  @Override
  public void deleteAvailabilityForFromDate(String restaurantId, LocalDate date) {
    datastore
        .find(Availability.class)
        .filter(eq("restaurantId", restaurantId))
        .filter(gte("date", date))
        .delete(new DeleteOptions().multi(true));
  }
}
