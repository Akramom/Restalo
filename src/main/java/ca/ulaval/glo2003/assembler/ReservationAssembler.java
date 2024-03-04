package ca.ulaval.glo2003.assembler;

import ca.ulaval.glo2003.dtos.ReservationDto;
import ca.ulaval.glo2003.entity.Reservation;

public class ReservationAssembler {

  public ReservationAssembler() {}

  public ReservationDto toDto(Reservation reservation) {
    return new ReservationDto(
        reservation.getId(),
        reservation.getDate(),
        reservation.getStartTime(),
        reservation.getEndTime(),
        reservation.getEndTime().getMinute() - reservation.getStartTime().getMinute(),
        reservation.getGroupSize(),
        reservation.getCustomer());
  }

  public Reservation fromDto(ReservationDto reservationDto) {
    return new Reservation(
        reservationDto.id(),
        reservationDto.date(),
        reservationDto.startTime(),
        reservationDto.durationInMin(),
        reservationDto.groupSize(),
        reservationDto.customer());
  }
}
