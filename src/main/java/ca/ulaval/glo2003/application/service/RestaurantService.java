package ca.ulaval.glo2003.application.service;

import static ca.ulaval.glo2003.util.Constante.NUMBER_OF_PLACES_UNAVAILABLE;

import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.application.assembler.SearchAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.dtos.SearchDto;
import ca.ulaval.glo2003.application.dtos.UpdateRestaurantDto;
import ca.ulaval.glo2003.application.validator.RestaurantValidator;
import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.domain.search.SearchRestaurantHelper;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.util.Util;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {

  private final IRestaurantRepository restaurantRepository;
  private final RestaurantValidator restaurantValidator;
  private final RestaurantAssembler restaurantAssembler;
  private final ReservationAssembler reservationAssembler;
  private final SearchRestaurantHelper searchHelper;
  private ReservationService reservationService;

  public ReservationService getReservationService() {
    return reservationService;
  }

  private AvailabilityService availabilityService;

  public void setAvailabilityService(AvailabilityService availabilityService) {
    this.availabilityService = availabilityService;
  }

  public void setReservationService(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  public RestaurantService(IRestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
    this.restaurantValidator = new RestaurantValidator();
    this.restaurantAssembler = new RestaurantAssembler();
    this.reservationAssembler = new ReservationAssembler();
    this.searchHelper = new SearchRestaurantHelper();
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

  public AvailabilityService getAvailabilityService() {
    return availabilityService;
  }

  public ReservationDto addReservation(ReservationDto reservationDto, String restaurantId)
      throws NotFoundException, InvalidParameterException {

    int reservationDuration = this.getRestaurantReservationDuration(restaurantId);
    reservationDto.setStartTime(Util.ajustStartTimeToNext15Min(reservationDto.getStartTime()));
    reservationDto.setEndTime(
        Util.calculEndTime(reservationDto.getStartTime(), reservationDuration));

    this.reservationService.verifyValidReservationParameter(restaurantId, reservationDto);
    Reservation reservation = reservationAssembler.fromDto(reservationDto);
    if (!this.availabilityService.reserveAvailabilities(reservation, restaurantId))
      throw new InvalidParameterException(NUMBER_OF_PLACES_UNAVAILABLE);

    Reservation addedReservation = restaurantRepository.addReservation(reservation, restaurantId);
    return new ReservationAssembler().toDto(addedReservation);
  }

  public int getRestaurantReservationDuration(String restaurantId) throws NotFoundException {
    return restaurantRepository.getRestaurantById(restaurantId).getReservation().duration();
  }

  public List<RestaurantDto> searchRestaurant(SearchDto searchDto) {

    List<Restaurant> allRestaurant = restaurantRepository.getAllRestaurants();

    List<Restaurant> restaurants =
        searchHelper.searchRestaurant(allRestaurant, new SearchAssembler().fromDto(searchDto));

    return restaurants.stream().map(this.restaurantAssembler::toDto).collect(Collectors.toList());
  }

  public void deleteRestaurantIfOwner(String restaurantId, String ownerId)
      throws NotFoundException {
    restaurantRepository.deleteRestaurantIfOwner(restaurantId, ownerId);
  }

  public void updateRestaurant(
      String ownerId, String restaurantId, UpdateRestaurantDto updateRestaurantDto)
      throws Exception {

    Restaurant oldRestaurant = restaurantRepository.getOwnerRestaurantById(ownerId, restaurantId);
    LocalDate actualDate = LocalDate.now();
    List<Reservation> reservations =
        restaurantRepository.getReservationsByRestaurantId(ownerId, restaurantId).stream()
            .filter(reservation -> reservation.getDate().isAfter(actualDate))
            .toList();
    removeReservationBeforeUpdate(restaurantId, reservations, actualDate);
    Restaurant updatedRestaurant = createUpdateRestaurant(updateRestaurantDto, oldRestaurant);
    updateRestaurantDetails(updatedRestaurant);
    List<Reservation> reservationToAdd = getValidReservations(reservations, updatedRestaurant);
    addReservations(reservationToAdd, updatedRestaurant);
  }

  private void removeReservationBeforeUpdate(
      String restaurantId, List<Reservation> reservations, LocalDate actualDate)
      throws NotFoundException {
    for (Reservation reservation : reservations) {
      reservationService.deleteReservation(reservation.getNumber());
    }
    availabilityService.deleteAvailabilityForFromDate(restaurantId, actualDate);
  }

  private void addReservations(List<Reservation> reservationToAdd, Restaurant updatedRestaurant)
      throws NotFoundException, InvalidParameterException {
    for (Reservation reservation : reservationToAdd) {
      addReservation(reservationAssembler.toDto(reservation), updatedRestaurant.getId());
      availabilityService.reserveAvailabilities(reservation, updatedRestaurant.getId());
    }
  }

  private static List<Reservation> getValidReservations(
      List<Reservation> reservations, Restaurant updatedRestaurant) {
    reservations.stream()
        .peek(
            reservation ->
                reservation.setEndTime(
                    Util.calculEndTime(
                        reservation.getStartTime(),
                        updatedRestaurant.getReservation().duration())));

    reservations =
        reservations.stream().sorted(Comparator.comparing(Reservation::getDate)).toList();

    reservations =
        reservations.stream()
            .filter(
                reservation ->
                    reservation.getStartTime().isBefore(updatedRestaurant.getHours().getClose())
                        && !reservation
                            .getEndTime()
                            .isAfter(updatedRestaurant.getHours().getClose())
                        && !reservation
                            .getStartTime()
                            .isBefore(updatedRestaurant.getHours().getOpen()))
            .toList();

    List<Reservation> reservationToAdd = new ArrayList<>();
    int restaurantCapacity = updatedRestaurant.getCapacity();

    for (Reservation reservation : reservations) {
      restaurantCapacity = restaurantCapacity - reservation.getGroupSize();
      if (restaurantCapacity < 0) break;
      reservationToAdd.add(reservation);
    }
    return reservationToAdd;
  }

  private void updateRestaurantDetails(Restaurant updatedRestaurant) throws Exception {
    this.verifyRestaurantParameter(restaurantAssembler.toDto(updatedRestaurant));
    restaurantRepository.updateRestaurant(updatedRestaurant);
  }

  private Restaurant createUpdateRestaurant(
      UpdateRestaurantDto updateRestaurantDto, Restaurant oldRestaurant) {
    Restaurant updatedRestaurant = new Restaurant(oldRestaurant);

    Hours hours =
        updateRestaurantDto.hours() == null
            ? oldRestaurant.getHours()
            : new Hours(
                updateRestaurantDto.hours().getOpen() == null
                    ? oldRestaurant.getHours().getOpen()
                    : updateRestaurantDto.hours().getOpen(),
                updateRestaurantDto.hours().getClose() == null
                    ? oldRestaurant.getHours().getClose()
                    : updateRestaurantDto.hours().getClose());
    updatedRestaurant.setHours(hours);
    updatedRestaurant.setName(
        updateRestaurantDto.name() == null ? oldRestaurant.getName() : updateRestaurantDto.name());
    updatedRestaurant.setCapacity(
        updateRestaurantDto.capacity() == 0
            ? oldRestaurant.getCapacity()
            : updateRestaurantDto.capacity());
    updatedRestaurant.setDuration(
        updateRestaurantDto.reservations() == null
            ? oldRestaurant.getReservation().duration()
            : updateRestaurantDto.reservations().duration() == 0
                ? oldRestaurant.getReservation().duration()
                : updateRestaurantDto.reservations().duration());
    return updatedRestaurant;
  }
}
