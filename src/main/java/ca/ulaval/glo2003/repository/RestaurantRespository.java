package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.entity.Owner;
import ca.ulaval.glo2003.entity.Reservation;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantRespository {

  private final List<Owner> owners;
  private final List<Restaurant> restaurants;

  public RestaurantRespository() {
    owners = new ArrayList<>();
    restaurants = new ArrayList<>();
  }

  public List<Owner> getOwner() {
    return owners;
  }

  public void addRestaurant(String ownerId, Restaurant restaurant) {

    owners.stream()
        .filter(owner -> owner.getOwnerId().equals(ownerId))
        .toList()
        .getFirst()
        .getRestaurants()
        .add(restaurant);
    restaurants.add(restaurant);

  }

  public Owner addOwner(String ownerId) {
    Owner owner = new Owner("Doe", "John", "418-222-2222");
    owner.setOwnerId(ownerId);
    owners.add(owner);
    return owner;
  }

  public Restaurant getOwnerRestaurantById(String ownerId, String restaurantId)
      throws NotFoundException {
    Restaurant unRestaurant =
        owners.stream()
            .filter(owner -> owner.getOwnerId().equals(ownerId))
            .toList()
            .getFirst()
            .getRestaurants()
            .stream()
            .filter(restaurant -> restaurant.getId().equals(restaurantId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("No restaurant found for the owner."));
    return unRestaurant;
  }

  public Restaurant getRestaurantById(String restaurantId) throws NotFoundException {
    Restaurant unRestaurant =
        owners.stream()
            .flatMap(owner -> owner.getRestaurants().stream())
            .toList()
            .stream()
            .filter(restaurant -> restaurant.getId().equals(restaurantId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("No restaurant found for the owner."));
    return unRestaurant;
  }

  public List<Restaurant> getAllRestaurants(String ownerId) {
    List<Restaurant> ownerRestaurants =
        owners.stream()
            .filter(owner -> owner.getOwnerId().equals(ownerId))
            .toList()
            .getFirst()
            .getRestaurants();
    return ownerRestaurants;
  }

  public Reservation addReservation(Reservation reservation, String restaurantId)
      throws NotFoundException {
    owners.stream()
        .flatMap(owner -> owner.getRestaurants().stream())
        .toList()
        .stream()
        .filter(restaurant -> restaurant.getId().equals(restaurantId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException("No restaurant found for the owner."))
        .addReservation(reservation);

    return getReservation(reservation.getId());
  }

  public Reservation getReservation(String reservationId) throws NotFoundException {
    return owners.stream()
        .flatMap(owner -> owner.getRestaurants().stream())
        .toList()
        .stream()
        .flatMap(restaurant -> restaurant.getReservationList().stream())
        .filter(reservation -> reservation.getId().equals(reservationId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException("reservation not found."));
  }
}
