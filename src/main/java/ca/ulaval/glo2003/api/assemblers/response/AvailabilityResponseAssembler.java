package ca.ulaval.glo2003.api.assemblers.response;

import ca.ulaval.glo2003.api.response.Availability.AvailabilityResponse;
import ca.ulaval.glo2003.application.dtos.AvailabilityDto;

public class AvailabilityResponseAssembler {
  public AvailabilityResponse fromDto(AvailabilityDto availabilityDto) {
    return new AvailabilityResponse(
        availabilityDto.getStart(), availabilityDto.getRemainingPlaces());
  }
}
