package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.entity.*;
import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.exception.MissingParameterException;
import ca.ulaval.glo2003.exception.NotFoundException;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.util.ReservationValidator;
import ca.ulaval.glo2003.util.RestaurantValidator;
import java.util.List;

public class RestaurantService {

  private final RestaurantRespository restaurantRepository;
  private final RestaurantValidator restaurantValidator;
  private final ReservationValidator reservationValidator;

  public RestaurantService(RestaurantRespository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
    this.restaurantValidator = new RestaurantValidator();
    this.reservationValidator = new ReservationValidator();
  }

  public Restaurant addRestaurant(String noOwner, Restaurant restaurant) {
    if (!isExistingOwnerId(noOwner)) {
      restaurantRepository.addOwner(noOwner);
    }
    restaurantRepository.addRestaurant(noOwner, restaurant);

    return restaurant;
  }

  public Restaurant getRestaurantByIdOfOwner(String ownerId, String restaurantId)
      throws NotFoundException {

    Restaurant restaurant = restaurantRepository.getRestaurant(ownerId, restaurantId);

    return restaurant;
  }

  public Restaurant getRestaurantByOwnerAndRestaurantId(String ownerId, String restaurantId)
      throws NotFoundException {
    return restaurantRepository.getRestaurant(ownerId, restaurantId);
  }

  public List<Restaurant> getAllRestaurantsOfOwner(String ownerId) {
    return restaurantRepository.getAllRestaurants(ownerId);
  }

  public Owner addNewOwner(String ownerId) {
    if (isExistingOwnerId(ownerId)) return null;
    return restaurantRepository.addOwner(ownerId);
  }

  public Boolean isExistingOwnerId(String OwnerId) {
    for (Owner owner : restaurantRepository.getOwner()) {
      String ownerId = owner.getOwnerId();
      if (OwnerId.equals(ownerId)) {
        return true;
      }
    }
    return false;
  }

  public boolean isValidOwnerId(String ownerId) throws Exception {
    if (this.restaurantValidator.isStringEmpty(ownerId)) {
      throw new MissingParameterException("Missing owner ID.");
    }
    return true;
  }

  public void verifyOwnerId(String ownerId) throws Exception {
    isValidOwnerId(ownerId);
  }

  public boolean verifyRestaurantParameter(Restaurant restaurant) throws Exception {

    if (this.restaurantValidator.isRestaurantParameterEmpty(restaurant))
      throw new MissingParameterException("Missing restaurant parameter.");

    if (!this.restaurantValidator.isValidRestaurant(restaurant))
      throw new InvalidParameterException("Invalid restaurant parameter.");

    return true;
  }

  public Restaurant verifyRestaurantId(String restaurantId)
      throws MissingParameterException, InvalidParameterException {
    if (this.reservationValidator.isStringEmpty(restaurantId)) {
      throw new MissingParameterException("Missing restaurant ID.");
    }
    if (invalidRestaurantId(restaurantId)) {
      throw new InvalidParameterException("Invalid restaurant ID.");
    }
    return restaurantRepository.getRestaurantById(restaurantId);
  }

  public Boolean invalidRestaurantId(String restaurantId) {
    return restaurantRepository.getRestaurantById(restaurantId) == null;
  }

  public void verifyEmptyReservationParameter(Reservation reservation)
      throws MissingParameterException {
    reservationValidator.isEmptyReservationParameter(reservation);
  }

  public void verifyValidReservationParameter(Restaurant restaurant, Reservation reservation)
      throws InvalidParameterException {
    reservationValidator.validateReservationToRestaurant(reservation, restaurant);
  }

  public void addReservationToRestaurant(Reservation reservation, Restaurant restaurant) {
    restaurant.addReservation(reservation);
  }
}
