package ca.ulaval.glo2003.service;

import static ca.ulaval.glo2003.util.Constante.*;

import ca.ulaval.glo2003.Response.Reservation.ReservationResponse;
import ca.ulaval.glo2003.Response.Restaurant.RestaurantOwnerResponse;
import ca.ulaval.glo2003.Response.Restaurant.RestaurantResponse;
import ca.ulaval.glo2003.entity.*;
import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.exception.MissingParameterException;
import ca.ulaval.glo2003.exception.NotFoundException;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.util.ReservationValidator;
import ca.ulaval.glo2003.util.RestaurantValidator;
import java.util.List;
import java.util.stream.Collectors;

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

  public RestaurantResponse getRestaurantByIdOfOwner(String ownerId, String restaurantId)
      throws NotFoundException {

    Restaurant restaurant = restaurantRepository.getOwnerRestaurantById(ownerId, restaurantId);
    RestaurantResponse restaurantResponse =
        new RestaurantOwnerResponse(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getCapacity(),
            restaurant.getHours(),
            restaurant.getReservation());

    return restaurantResponse;
  }

  public Restaurant getRestaurantByOwnerAndRestaurantId(String ownerId, String restaurantId)
      throws NotFoundException {
    return restaurantRepository.getOwnerRestaurantById(ownerId, restaurantId);
  }

  public List<RestaurantOwnerResponse> getAllRestaurantsOfOwner(String ownerId) {

    return restaurantRepository.getAllRestaurants(ownerId).stream()
        .map(
            restaurant ->
                new RestaurantOwnerResponse(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getCapacity(),
                    restaurant.getHours(),
                    restaurant.getReservation()))
        .collect(Collectors.toList());
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
      throw new MissingParameterException(MISSING_OWNER_ID);
    }
    return true;
  }

  public void verifyOwnerId(String ownerId) throws Exception {
    isValidOwnerId(ownerId);
  }

  public boolean verifyRestaurantParameter(Restaurant restaurant) throws Exception {

    if (this.restaurantValidator.isRestaurantParameterEmpty(restaurant))
      throw new MissingParameterException(MISSING_RESTAURANT_PARAMETER);

    if (!this.restaurantValidator.isValidRestaurant(restaurant))
      throw new InvalidParameterException(INVALID_RESTAURANT_PARAMETER);

    return true;
  }

  public Restaurant verifyExistRestaurant(String restaurantId)
      throws MissingParameterException, NotFoundException {
    if (this.restaurantValidator.isStringEmpty(restaurantId)) {
      throw new MissingParameterException(MISSING_RESTAURANT_ID);
    }
    return restaurantRepository.getRestaurantById(restaurantId);
  }

  public void verifyReservationNumber(String reservationId) throws MissingParameterException {
    if (this.reservationValidator.isStringEmpty(reservationId)) {
      throw new MissingParameterException(MISSING_RESERVATION_NUMBER);
    }
  }

  public void verifyEmptyReservationParameter(Reservation reservation)
      throws MissingParameterException {
    reservationValidator.isEmptyReservationParameter(reservation);
  }

  public void verifyValidReservationParameter(String restaurantId, Reservation reservation)
      throws InvalidParameterException, NotFoundException {
    Restaurant restaurant = restaurantRepository.getRestaurantById(restaurantId);
    reservationValidator.validateReservationToRestaurant(
        reservation, restaurant.getHours().getClose());
  }

  public Reservation addReservation(Reservation reservation, String restaurantId)
      throws NotFoundException {
    return restaurantRepository.addReservation(reservation, restaurantId);
  }

  public int getRestaurantReservationDuration(String restaurantId) throws NotFoundException {
    return restaurantRepository.getRestaurantById(restaurantId).getReservation().duration();
  }

  public ReservationResponse getReservationByNumber(String resevationNumber)
      throws NotFoundException {
    Reservation reservation = restaurantRepository.getReservationByNumber(resevationNumber);
    Restaurant restaurant = restaurantRepository.getRestaurantByReservationNumber(resevationNumber);

    RestaurantResponse restaurantResponse =
        new RestaurantResponse(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getCapacity(),
            restaurant.getHours());
    ReservationResponse reservationResponse =
        new ReservationResponse(
            reservation.getNumber(),
            reservation.getDate(),
            new Time(reservation.getStartTime(), reservation.getEndTime()),
            reservation.getGroupSize(),
            reservation.getCustomer(),
            restaurantResponse);

    return reservationResponse;
  }
}
