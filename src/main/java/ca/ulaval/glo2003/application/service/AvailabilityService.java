package ca.ulaval.glo2003.application.service;

import static ca.ulaval.glo2003.util.Constante.*;

import ca.ulaval.glo2003.application.assembler.AvailabilityAssembler;
import ca.ulaval.glo2003.application.dtos.AvailabilityDto;
import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.RestaurantRepository;
import ca.ulaval.glo2003.util.Util;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AvailabilityService {
  private final AvailabilityAssembler availabilityAssembler;

  private RestaurantRepository restaurantRepository;

  public AvailabilityService(RestaurantRepository restaurantRepository) {
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

  public void deleteAvailabilityForFromDate(String restaurantId, LocalDate date)
      throws NotFoundException {
    restaurantRepository.deleteAvailabilityForFromDate(restaurantId, date);
  }

  public void releaseAvailibilities(Reservation reservation, String restaurantId)
      throws NotFoundException {
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
                availabilityDto ->
                    availabilityDto.setRemainingPlaces(
                        availabilityDto.getRemainingPlaces() + reservation.getGroupSize()))
            .toList();

    availabilities.forEach(
        availabilityDto -> {
          restaurantRepository.updateAvailability(
              this.availabilityAssembler.fromDto(availabilityDto));
        });
  }

  public boolean reserveAvailabilities(Reservation reservation, String restaurantId)
      throws NotFoundException {

    List<Availability> availabilities = new ArrayList<>();

    for (Availability availability :
        getAvailabilities(restaurantId, reservation.getDate()).stream()
            .map(availabilityAssembler::fromDto)
            .toList()) {
      availabilities.add(
          new Availability(
              availability.getId(),
              availability.getRestaurantId(),
              availability.getStart(),
              availability.getRemainingPlaces()));
    }

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
                availability ->
                    availability.setRemainingPlaces(
                        availability.getRemainingPlaces() - reservation.getGroupSize()))
            .toList();

    boolean hasInvalidAvailability =
        availabilities.stream().anyMatch(availability -> availability.getRemainingPlaces() < 0);

    if (hasInvalidAvailability) {
      return false;
    }

    availabilities.forEach(restaurantRepository::updateAvailability);

    return true;
  }
}
