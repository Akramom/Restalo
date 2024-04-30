package ca.ulaval.glo2003.application.service;

import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.validator.ReservationValidator;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.domain.search.SearchReservationHelper;
import ca.ulaval.glo2003.repository.RestaurantRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
  private final RestaurantRepository restaurantRepository;
  private final ReservationValidator reservationValidator;
  private final SearchReservationHelper searchHelper;
  private final ReservationAssembler reservationAssembler;

  private AvailabilityService availabilityService;

  public void setAvailabilityService(AvailabilityService availabilityService) {
    this.availabilityService = availabilityService;
  }

  public ReservationService(RestaurantRepository restaurantRespository) {
    this.restaurantRepository = restaurantRespository;
    this.reservationValidator = new ReservationValidator();
    this.searchHelper = new SearchReservationHelper();
    this.reservationAssembler = new ReservationAssembler();
  }

  public ReservationDto getReservationByNumber(String reservationNumber) throws NotFoundException {
    Reservation reservation = restaurantRepository.getReservationByNumber(reservationNumber);
    Restaurant restaurant =
        restaurantRepository.getRestaurantByReservationNumber(reservationNumber);
    return new ReservationAssembler().toDto(reservation, restaurant);
  }

  public void verifyValidReservationParameter(String restaurantId, ReservationDto reservationDto)
      throws InvalidParameterException, NotFoundException {
    Restaurant restaurant = restaurantRepository.getRestaurantById(restaurantId);
    reservationValidator.validateReservationToRestaurant(
        reservationDto,
        restaurant.getHours().getOpen(),
        restaurant.getHours().getClose(),
        restaurant.getCapacity());
  }

  public void deleteReservation(String reservationNumber) throws NotFoundException {
    Reservation reservation = restaurantRepository.getReservationByNumber(reservationNumber);
    Restaurant restaurant =
        restaurantRepository.getRestaurantByReservationNumber(reservationNumber);
    this.availabilityService.releaseAvailibilities(reservation, restaurant.getId());
    restaurantRepository.deleteReservation(reservationNumber, restaurant.getId());
  }

  public List<ReservationDto> searchReservation(
      String ownerId, String restaurantId, String dateStr, String customName)
      throws NotFoundException {

    List<Reservation> allReservations =
        restaurantRepository.getRerservationsByRestaurantId(ownerId, restaurantId);
    List<Reservation> reservations =
        searchHelper.searchReservation(allReservations, dateStr, customName);

    return reservations.stream().map(this.reservationAssembler::toDto).collect(Collectors.toList());
  }
}
