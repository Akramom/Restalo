package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.RESERVATION_NOT_FOUND;
import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;

import ca.ulaval.glo2003.domain.entity.Owner;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.entity.Search;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantRepository {

  private List<Owner> owners;

  public RestaurantRepository() {
    owners = new ArrayList<>();
  }

  public List<Owner> getOwner() {
    return owners;
  }

  public Restaurant addRestaurant(String ownerId, Restaurant restaurant) {

    if (restaurant.getId() == null) {
      restaurant.setId(Util.generateId());
    }
    owners.stream()
        .filter(owner -> owner.getOwnerId().equals(ownerId))
        .toList()
        .getFirst()
        .getRestaurants()
        .add(restaurant);

    return restaurant;
  }

  public Owner addOwner(String ownerId) {
    Owner owner = new Owner(ownerId, "Doe", "John", "418-222-2222");
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
            .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND));
    return unRestaurant;
  }

  public Restaurant getRestaurantById(String restaurantId) throws NotFoundException {
    Restaurant unRestaurant =
        owners.stream().flatMap(owner -> owner.getRestaurants().stream()).toList().stream()
            .filter(restaurant -> restaurant.getId().equals(restaurantId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND));
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

    if (reservation.getNumber() == null) reservation.setNumber(Util.generateId());
    owners.stream()
        .flatMap(owner -> owner.getRestaurants().stream())
        .collect(Collectors.toList())
        .stream()
        .filter(restaurant -> restaurant.getId().equals(restaurantId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND))
        .addReservation(reservation);

    return getReservationByNumber(reservation.getNumber());
  }

  public Reservation getReservationByNumber(String reservationNumer) throws NotFoundException {
    return owners.stream()
        .flatMap(owner -> owner.getRestaurants().stream())
        .collect(Collectors.toList())
        .stream()
        .flatMap(restaurant -> restaurant.getReservationList().stream())
        .filter(reservation -> reservation.getNumber().equals(reservationNumer))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND));
  }

  public Restaurant getRestaurantByReservationNumber(String number) throws NotFoundException {

    Restaurant unRestaurant =
        owners.stream()
            .flatMap(owner -> owner.getRestaurants().stream())
            .collect(Collectors.toList())
            .stream()
            .filter(
                restaurant ->
                    restaurant.getReservationList().stream()
                        .anyMatch(reservation -> reservation.getNumber().equals(number)))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND));
    return unRestaurant;
  }

  public List<Restaurant> search(Search search) {
    List<Restaurant> allRestaurant =
        owners.stream()
            .flatMap(owner -> owner.getRestaurants().stream())
            .collect(Collectors.toList());

    if (Util.isSearchDtoNull(search)) {
      return allRestaurant;
    }

    if (Util.isSearchDtoOpenedNull(search)) {
      return Util.searchByName(allRestaurant, search.getName());
    }

    if (Util.isSearchDtoNameNull(search)) {
      return Util.searchByOpenedHour(allRestaurant, search.getOpened());
    }

    return Util.search(allRestaurant, search);
  }
}
