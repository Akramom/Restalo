package ca.ulaval.glo2003.application.assembler;

import ca.ulaval.glo2003.application.dtos.AvailabilityDto;
import ca.ulaval.glo2003.domain.entity.Availability;

public class AvailabilityAssembler {

  public Availability fromDto(AvailabilityDto availabilityDto) {
    return new Availability(
        availabilityDto.getId(),
        availabilityDto.getRestaurantId(),
        availabilityDto.getStart(),
        availabilityDto.getRemainingPlaces());
  }

  public AvailabilityDto toDto(Availability availability) {
    return new AvailabilityDto(
        availability.getId(),
        availability.getRestaurantId(),
        availability.getStart(),
        availability.getRemainingPlaces());
  }
}
