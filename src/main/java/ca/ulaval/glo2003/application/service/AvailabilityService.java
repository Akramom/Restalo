package ca.ulaval.glo2003.application.service;

import static ca.ulaval.glo2003.util.Constante.*;

import ca.ulaval.glo2003.application.assembler.AvailabilityAssembler;
import ca.ulaval.glo2003.application.dtos.AvailabilityDto;
import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.IRestaurantRepository;
import ca.ulaval.glo2003.util.Util;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AvailabilityService {
  private final AvailabilityAssembler availabilityAssembler;

  private IRestaurantRepository restaurantRepository;

  public AvailabilityService(IRestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
    this.availabilityAssembler = new AvailabilityAssembler();
  }

  public List<AvailabilityDto> getAvailabilities(String restaurantId, LocalDate date)
      throws NotFoundException {

    if (!restaurantRepository.isExistAvailabilityForADate(restaurantId, date))
      restaurantRepository.addAvailabilitiesForADate(restaurantId, date);

    return restaurantRepository.getAvailabilitiesForADate(restaurantId, date).stream()
        .map(availabilityAssembler::toDto)
        .collect(Collectors.toList());
  }

  public void releaseAvailibilities(Reservation reservation, String restaurantId)
      throws NotFoundException {
    AtomicInteger remainingPlaces = new AtomicInteger();
    List<AvailabilityDto> availabilities = getAvailabilities(restaurantId, reservation.getDate());

    availabilities =
        availabilities.stream()
            .filter(
                availabilityDto ->
                    !availabilityDto.getStart().toLocalTime().isBefore(reservation.getStartTime())
                        && !availabilityDto
                            .getStart()
                            .toLocalTime()
                            .isAfter(
                                Util.adjustToPrevious15Minutes(
                                    reservation.getEndTime().minusMinutes(1))))
            .peek(
                availabilityDto -> {
                  remainingPlaces.set(
                      availabilityDto.getRemainingPlaces() + reservation.getGroupSize());

                  availabilityDto.setRemainingPlaces(remainingPlaces.get());
                })
            .toList();

    availabilities.forEach(
        availabilityDto -> {
          restaurantRepository.updateAvailability(
              this.availabilityAssembler.fromDto(availabilityDto));
        });
  }

  public void reserveAvailabilities(Reservation reservation, String restaurantId)
      throws NotFoundException, InvalidParameterException {

    List<Availability> availabilities =
        restaurantRepository.getAvailabilitiesForADate(restaurantId, reservation.getDate());

    AtomicInteger remainingPlaces = new AtomicInteger();
    availabilities =
        availabilities.stream()
            .filter(
                availability ->
                    !availability.getStart().toLocalTime().isBefore(reservation.getStartTime())
                        && !availability
                            .getStart()
                            .toLocalTime()
                            .isAfter(
                                Util.adjustToPrevious15Minutes(
                                    reservation.getEndTime().minusMinutes(1))))
            .peek(
                availability -> {
                  remainingPlaces.set(
                      availability.getRemainingPlaces() - reservation.getGroupSize());

                  availability.setRemainingPlaces(remainingPlaces.get());
                })
            .toList();

    if (remainingPlaces.get() < 0) {
      throw new InvalidParameterException(NUMBER_OF_PLACES_UNAVAILABLE);
    }

    availabilities.forEach(restaurantRepository::updateAvailability);
  }
}
