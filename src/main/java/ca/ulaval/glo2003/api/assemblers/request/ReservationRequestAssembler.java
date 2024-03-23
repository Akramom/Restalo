package ca.ulaval.glo2003.api.assemblers.request;

import ca.ulaval.glo2003.api.request.ReservationRequest;
import ca.ulaval.glo2003.application.dtos.CustomerDto;
import ca.ulaval.glo2003.application.dtos.ReservationDto;

public class ReservationRequestAssembler {
  public ReservationDto toDto(ReservationRequest reservationRequest) {
    return new ReservationDto(
        reservationRequest.getNumber(),
        reservationRequest.getDate(),
        reservationRequest.getStartTime(),
        reservationRequest.getEndTime(),
        reservationRequest.getDurationInMin(),
        reservationRequest.getGroupSize(),
        new CustomerDto(
            reservationRequest.getCustomer().getName(),
            reservationRequest.getCustomer().getEmail(),
            reservationRequest.getCustomer().getPhoneNumber()));
  }
}
