package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.*;

import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.entity.Owner;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Util;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantRepositoryInMemory implements IRestaurantRepository {

  private List<Owner> owners;

  public RestaurantRepositoryInMemory() {
    owners = new ArrayList<>();
  }

  @Override
  public List<Owner> getOwners() {
    return owners;
  }

  @Override
  public Owner getOwner(String ownerId) throws NotFoundException {
    return owners.stream()
        .filter(owner -> owner.getOwnerId().equals(ownerId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(OWNER_NOT_FOUND));
  }

  @Override
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

  @Override
  public Owner addOwner(String ownerId) {
    Owner owner = new Owner(ownerId);
    owners.add(owner);
    return owner;
  }

  @Override
  public Restaurant getOwnerRestaurantById(String ownerId, String restaurantId)
      throws NotFoundException {
    Restaurant wantedRestaurant =
        owners.stream()
            .filter(owner -> owner.getOwnerId().equals(ownerId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(OWNER_NOT_FOUND))
            .getRestaurants()
            .stream()
            .filter(restaurant -> restaurant.getId().equals(restaurantId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND));
    return wantedRestaurant;
  }

  @Override
  public Restaurant getRestaurantById(String restaurantId) throws NotFoundException {
    Restaurant wantedRestaurant =
        owners.stream().flatMap(owner -> owner.getRestaurants().stream()).toList().stream()
            .filter(restaurant -> restaurant.getId().equals(restaurantId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND));
    return wantedRestaurant;
  }

  @Override
  public List<Restaurant> getAllOwnerRestaurants(String ownerId) throws NotFoundException {
    List<Restaurant> ownerRestaurants =
        owners.stream()
            .filter(owner -> owner.getOwnerId().equals(ownerId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(OWNER_NOT_FOUND))
            .getRestaurants();
    return ownerRestaurants;
  }

  @Override
  public List<Restaurant> getAllRestaurants() {
    List<Restaurant> allRestaurant =
        owners.stream()
            .flatMap(owner -> owner.getRestaurants().stream())
            .collect(Collectors.toList());
    return allRestaurant;
  }

  @Override
  public Reservation addReservation(Reservation reservation, String restaurantId)
      throws NotFoundException {

    if (reservation.getNumber() == null) reservation.setNumber(Util.generateId());
    owners.stream().flatMap(owner -> owner.getRestaurants().stream()).toList().stream()
        .filter(restaurant -> restaurant.getId().equals(restaurantId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND))
        .addReservation(reservation);

    return getReservationByNumber(reservation.getNumber());
  }

  @Override
  public Reservation getReservationByNumber(String reservationNumber) throws NotFoundException {
    return owners.stream()
        .flatMap(owner -> owner.getRestaurants().stream())
        .collect(Collectors.toList())
        .stream()
        .flatMap(restaurant -> restaurant.getReservationList().stream())
        .filter(reservation -> reservation.getNumber().equals(reservationNumber))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND));
  }

  @Override
  public Restaurant getRestaurantByReservationNumber(String number) throws NotFoundException {

    Restaurant wantedRestaurant =
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
    return wantedRestaurant;
  }

  @Override
  public void deleteRestaurantIfOwner(String restaurantId, String ownerId)
      throws NotFoundException {
    getOwnerRestaurantById(ownerId, restaurantId);

    owners.stream()
        .filter(owner -> owner.getOwnerId().equals(ownerId))
        .toList()
        .getFirst()
        .getRestaurants()
        .remove(
            owners.stream()
                .filter(owner -> owner.getOwnerId().equals(ownerId))
                .toList()
                .getFirst()
                .getRestaurants()
                .stream()
                .filter(restaurant -> restaurant.getId().equals(restaurantId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND)));
  }

  public boolean isExistAvailabilityForADate(String restaurantId, LocalDate date)
      throws NotFoundException {
    return !getAvailabilitiesForADate(restaurantId, date).isEmpty();
  }

  public List<Availability> getAvailabilitiesForADate(String restaurantId, LocalDate date)
      throws NotFoundException {
    return getRestaurantById(restaurantId).getAvailabilities().stream()
        .filter(availability -> availability.getStart().toLocalDate().equals(date))
        .toList();
  }

  public void addAvailabilitiesForADate(String restaurantId, LocalDate date) {
    owners.stream().flatMap(owner -> owner.getRestaurants().stream()).toList().stream()
        .filter(restaurant -> restaurant.getId().equals(restaurantId))
        .toList()
        .getFirst()
        .addAvailabilities(date);
  }

  public void updateAvailability(Availability updatedAvailability) {
    owners.stream().flatMap(owner -> owner.getRestaurants().stream()).toList().stream()
        .filter(restaurant -> restaurant.getId().equals(updatedAvailability.getRestaurantId()))
        .findFirst()
        .ifPresent(restaurant -> restaurant.setAvailability(updatedAvailability));
  }

  @Override
  public void deleteReservation(String reservationNumber, String restaurantId)
      throws NotFoundException {
    owners.stream().flatMap(owner -> owner.getRestaurants().stream()).toList().stream()
        .filter(restaurant -> restaurant.getId().equals(restaurantId))
        .findFirst()
        .ifPresent(restaurant -> restaurant.removeReservation(reservationNumber));
  }

  @Override
  public List<Reservation> getRerservationsByRestaurantId(String ownerId, String restaurantId)
      throws NotFoundException {
    return owners.stream()
        .filter(owner -> owner.getOwnerId().equals(ownerId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(OWNER_NOT_FOUND))
        .getRestaurants()
        .stream()
        .filter(restaurant -> restaurant.getId().equals(restaurantId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(RESTAURANT_NOT_FOUND))
        .getReservationList();
  }

  @Override
  public void updateReservation(Reservation updatedReservation) {
    owners.stream()
        .flatMap(owner -> owner.getRestaurants().stream())
        .flatMap(restaurant -> restaurant.getReservationList().stream())
        .filter(reservation -> reservation.getNumber().equals(updatedReservation.getNumber()))
        .findFirst()
        .map(
            reservation -> {
              reservation.setDate(updatedReservation.getDate());
              reservation.setStartTime(updatedReservation.getStartTime());
              reservation.setEndTime(updatedReservation.getEndTime());
              reservation.setGroupSize(updatedReservation.getGroupSize());
              return reservation;
            });
  }
}
