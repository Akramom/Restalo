package ca.ulaval.glo2003.api.assemblers.request;

import ca.ulaval.glo2003.api.request.ReservationRequest;
import ca.ulaval.glo2003.api.request.UpdateReservationRequest;
import ca.ulaval.glo2003.application.dtos.CustomerDto;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.UpdateReservationDto;

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

  public UpdateReservationDto toDto(UpdateReservationRequest updateReservationRequest) {
    return new UpdateReservationDto(
        updateReservationRequest.getDate(),
        updateReservationRequest.getStartTime(),
        updateReservationRequest.getGroupSize());
  }
}
