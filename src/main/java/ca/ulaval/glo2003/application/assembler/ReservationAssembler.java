package ca.ulaval.glo2003.application.assembler;

import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.domain.entity.Reservation;

public class ReservationAssembler {

  public ReservationAssembler() {}

  public Reservation fromDto(ReservationDto reservationDto) {
    return new Reservation(
        reservationDto.getNumber(),
        reservationDto.getDate(),
        reservationDto.getStartTime(),
        reservationDto.getEndTime(),
        reservationDto.getGroupSize(),
        reservationDto.getCustomer());
  }
}
