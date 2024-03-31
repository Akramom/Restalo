package ca.ulaval.glo2003.application.service;

import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.validator.ReservationValidator;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.IRestaurantRepository;

public class ReservationService {
  private final IRestaurantRepository restaurantRepository;
  private final ReservationValidator reservationValidator;

  private AvailabilityService availabilityService;

  public void setAvailabilityService(AvailabilityService availabilityService) {
    this.availabilityService = availabilityService;
  }

  public ReservationService(IRestaurantRepository restaurantRespository) {
    this.restaurantRepository = restaurantRespository;
    this.reservationValidator = new ReservationValidator();
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
        reservationDto, restaurant.getHours().getClose());
  }

  public void deleteReservation(String reservationNumber) throws NotFoundException {
    Reservation reservation = restaurantRepository.getReservationByNumber(reservationNumber);
    Restaurant restaurant =
        restaurantRepository.getRestaurantByReservationNumber(reservationNumber);
    this.availabilityService.releaseAvailibilities(reservation, restaurant.getId());
    restaurantRepository.deleteReservation(reservationNumber, restaurant.getId());
  }
}
