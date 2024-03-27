package ca.ulaval.glo2003.application.service;

import static ca.ulaval.glo2003.util.Constante.*;

import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.application.assembler.SearchAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.dtos.SearchDto;
import ca.ulaval.glo2003.application.validator.ReservationValidator;
import ca.ulaval.glo2003.application.validator.RestaurantValidator;
import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.domain.search.SearchHelper;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.util.Util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {

  private final IRestaurantRepository restaurantRepository;
  private final RestaurantValidator restaurantValidator;
  private final ReservationValidator reservationValidator;
  private final RestaurantAssembler restaurantAssembler;
  private final ReservationAssembler reservationAssembler;

  private final SearchHelper searchHelper;

  public RestaurantService(IRestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
    this.restaurantValidator = new RestaurantValidator();
    this.reservationValidator = new ReservationValidator();
    this.restaurantAssembler = new RestaurantAssembler();
    this.reservationAssembler = new ReservationAssembler();
    this.searchHelper = new SearchHelper();
  }

  public RestaurantDto addRestaurant(String ownerId, RestaurantDto restaurantDto) throws Exception {
    this.verifyRestaurantParameter(restaurantDto);
    this.addOwnerIfNew(ownerId);
    Restaurant addRestaurant =
        restaurantRepository.addRestaurant(ownerId, restaurantAssembler.fromDto(restaurantDto));

    return restaurantAssembler.toDto(addRestaurant);
  }

  public RestaurantDto getRestaurantByIdOfOwner(String ownerId, String restaurantId)
      throws Exception {

    Restaurant restaurant = restaurantRepository.getOwnerRestaurantById(ownerId, restaurantId);

    return restaurantAssembler.toDto(restaurant);
  }

  public List<RestaurantDto> getAllRestaurantsOfOwner(String ownerId) throws Exception {

    return restaurantRepository.getAllOwnerRestaurants(ownerId).stream()
        .map(this.restaurantAssembler::toDto)
        .collect(Collectors.toList());
  }

  public Owner addOwnerIfNew(String ownerId) {
    if (isExistingOwnerId(ownerId)) return null;
    return restaurantRepository.addOwner(ownerId);
  }

  public Boolean isExistingOwnerId(String OwnerId) {
    for (Owner owner : restaurantRepository.getOwners()) {
      String ownerId = owner.getOwnerId();
      if (OwnerId.equals(ownerId)) {
        return true;
      }
    }
    return false;
  }

  public void verifyRestaurantParameter(RestaurantDto restaurant) throws Exception {
    this.restaurantValidator.validRestaurant(restaurant);
  }

  public void verifyExistRestaurant(String restaurantId) throws NotFoundException {
    restaurantRepository.getRestaurantById(restaurantId);
  }

  public void verifyValidReservationParameter(String restaurantId, ReservationDto reservationDto)
      throws InvalidParameterException, NotFoundException {
    Restaurant restaurant = restaurantRepository.getRestaurantById(restaurantId);
    reservationValidator.validateReservationToRestaurant(
        reservationDto, restaurant.getHours().getClose());
  }

  public ReservationDto addReservation(ReservationDto reservationDto, String restaurantId)
      throws NotFoundException, InvalidParameterException {
    this.verifyExistRestaurant(restaurantId);

    int reservationDuration = this.getRestaurantReservationDuration(restaurantId);
    reservationDto.setStartTime(Util.ajustStartTimeToNext15Min(reservationDto.getStartTime()));
    reservationDto.setEndTime(
        Util.calculEndTime(reservationDto.getStartTime(), reservationDuration));

    this.verifyValidReservationParameter(restaurantId, reservationDto);

    Reservation reservation = reservationAssembler.fromDto(reservationDto);
    Reservation addedReservation = restaurantRepository.addReservation(reservation, restaurantId);

    return new ReservationAssembler().toDto(addedReservation);
  }

  public int getRestaurantReservationDuration(String restaurantId) throws NotFoundException {
    return restaurantRepository.getRestaurantById(restaurantId).getReservation().duration();
  }

  public ReservationDto getReservationByNumber(String reservationNumber) throws NotFoundException {
    Reservation reservation = restaurantRepository.getReservationByNumber(reservationNumber);
    Restaurant restaurant =
        restaurantRepository.getRestaurantByReservationNumber(reservationNumber);
    return new ReservationAssembler().toDto(reservation, restaurant);
  }

  public List<RestaurantDto> searchRestaurant(SearchDto searchDto) {

    List<Restaurant> allRestaurant = restaurantRepository.getAllRestaurants();

    List<Restaurant> restaurants =
        searchHelper.searchRestaurant(allRestaurant, new SearchAssembler().fromDto(searchDto));

    return restaurants.stream().map(this.restaurantAssembler::toDto).collect(Collectors.toList());
  }
  public List<Availability> calculateAvailabilities(String restaurantId, LocalDate date) throws NotFoundException {
    Restaurant restaurant = restaurantRepository.getRestaurantById(restaurantId);
    List<Availability> availabilities = new ArrayList<>();
    LocalTime time = restaurant.getHours().getOpen().plusMinutes(15 - (restaurant.getHours().getOpen().getMinute() % 15));
    while (time.isBefore(restaurant.getHours().getClose().minusMinutes(restaurant.getDuration()))) {
      int remainingPlaces = calculateRemainingPlaces(restaurant, date, time);
      availabilities.add(new Availability(time.toString(), remainingPlaces));
      time = time.plusMinutes(15);
    }
    return availabilities;
  }

  private int calculateRemainingPlaces(Restaurant restaurant, LocalDate date, LocalTime time) {
    int reservedPlaces = restaurant.getReservationList().stream()
            .filter(reservation -> reservation.getDate().equals(date) && reservation.getStartTime().equals(time))
            .mapToInt(Reservation::getGroupSize)
            .sum();
    return restaurant.getCapacity() - reservedPlaces;
  }

  public static LocalTime addToNext15MinSlot(LocalTime time) {
    int minutes = time.getMinute();
    int minutesOverThePrevious15MinSlot = minutes % 15;
    if (minutesOverThePrevious15MinSlot == 0) {
      return time;
    }
    return time.plusMinutes(15 - minutesOverThePrevious15MinSlot);
  }
  public LocalDate validateAndParseDate(String dateStr) throws IllegalArgumentException {
    if (dateStr == null || dateStr.isEmpty()) {
      throw new IllegalArgumentException("Date parameter is required");
    }
    try {
      return LocalDate.parse(dateStr);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format");
    }
  }
}
