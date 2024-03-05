package ca.ulaval.glo2003.application.service;

import static ca.ulaval.glo2003.util.Constante.*;

import ca.ulaval.glo2003.api.response.reservation.ReservationPartialResponse;
import ca.ulaval.glo2003.api.response.reservation.ReservationResponse;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantPartialResponse;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantResponse;
import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.dtos.SearchDto;
import ca.ulaval.glo2003.application.validator.ReservationValidator;
import ca.ulaval.glo2003.application.validator.RestaurantValidator;
import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.*;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final RestaurantValidator restaurantValidator;
  private final ReservationValidator reservationValidator;
  private final RestaurantAssembler restaurantAssembler;
  private final ReservationAssembler reservationAssembler;

  public RestaurantService(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
    this.restaurantValidator = new RestaurantValidator();
    this.reservationValidator = new ReservationValidator();
    this.restaurantAssembler = new RestaurantAssembler();
    this.reservationAssembler = new ReservationAssembler();
  }

  public RestaurantResponse addRestaurant(String noOwner, RestaurantDto restaurant) {
    if (!isExistingOwnerId(noOwner)) {
      restaurantRepository.addOwner(noOwner);
    }
    Restaurant addRestaurant =
        restaurantRepository.addRestaurant(noOwner, restaurantAssembler.fromDto(restaurant));

    return new RestaurantResponse(
        addRestaurant.getId(),
        addRestaurant.getName(),
        addRestaurant.getCapacity(),
        addRestaurant.getHours(),
        addRestaurant.getReservation());
  }

  public RestaurantPartialResponse getRestaurantByIdOfOwner(String ownerId, String restaurantId)
      throws NotFoundException {

    Restaurant restaurant = restaurantRepository.getOwnerRestaurantById(ownerId, restaurantId);
    RestaurantPartialResponse restaurantResponse =
        new RestaurantResponse(
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

  public List<RestaurantResponse> getAllRestaurantsOfOwner(String ownerId) {

    return restaurantRepository.getAllRestaurants(ownerId).stream()
        .map(
            restaurant ->
                new RestaurantResponse(
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

  public boolean verifyRestaurantParameter(RestaurantDto restaurant) throws Exception {

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

  public void verifyEmptyReservationParameter(ReservationDto reservationDto)
      throws MissingParameterException {
    reservationValidator.isEmptyReservationParameter(reservationDto);
  }

  public void verifyValidReservationParameter(String restaurantId, ReservationDto reservationDto)
      throws InvalidParameterException, NotFoundException {
    Restaurant restaurant = restaurantRepository.getRestaurantById(restaurantId);
    reservationValidator.validateReservationToRestaurant(
        reservationDto, restaurant.getHours().getClose());
  }

  public ReservationPartialResponse addReservation(
      ReservationDto reservationDto, String restaurantId) throws NotFoundException {
    Reservation reservation = reservationAssembler.fromDto(reservationDto);
    Reservation addedReservation = restaurantRepository.addReservation(reservation, restaurantId);

    return new ReservationPartialResponse(
        addedReservation.getNumber(),
        addedReservation.getDate(),
        new Time(addedReservation.getStartTime(), addedReservation.getEndTime()),
        addedReservation.getGroupSize(),
        reservation.getCustomer());
  }

  public int getRestaurantReservationDuration(String restaurantId) throws NotFoundException {
    return restaurantRepository.getRestaurantById(restaurantId).getReservation().duration();
  }

  public ReservationResponse getReservationByNumber(String resevationNumber)
      throws NotFoundException {
    Reservation reservation = restaurantRepository.getReservationByNumber(resevationNumber);
    Restaurant restaurant = restaurantRepository.getRestaurantByReservationNumber(resevationNumber);

    RestaurantPartialResponse restaurantResponse =
        new RestaurantPartialResponse(
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

  public List<RestaurantPartialResponse> searchRestaurant(SearchDto searchDto) {

    List<Restaurant> restaurants =
        restaurantRepository.search(new Search(searchDto.getName(), searchDto.getOpened()));

    return restaurants.stream()
        .map(
            restaurant ->
                new RestaurantPartialResponse(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getCapacity(),
                    restaurant.getHours()))
        .collect(Collectors.toList());
  }
}
