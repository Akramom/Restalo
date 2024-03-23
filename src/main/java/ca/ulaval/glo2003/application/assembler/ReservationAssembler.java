package ca.ulaval.glo2003.application.assembler;

import ca.ulaval.glo2003.application.dtos.CustomerDto;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.domain.entity.Customer;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;

public class ReservationAssembler {

  public ReservationAssembler() {}

  public Reservation fromDto(ReservationDto reservationDto) {
    return new Reservation(
        reservationDto.getNumber(),
        reservationDto.getDate(),
        reservationDto.getStartTime(),
        reservationDto.getEndTime(),
        reservationDto.getGroupSize(),
        new Customer(
            reservationDto.getCustomer().getName(),
            reservationDto.getCustomer().getEmail(),
            reservationDto.getCustomer().getPhoneNumber()));
  }

  public ReservationDto toDto(Reservation reservation) {
    return new ReservationDto(
        reservation.getNumber(),
        reservation.getDate(),
        reservation.getStartTime(),
        reservation.getEndTime(),
        reservation.getEndTime().getMinute() - reservation.getStartTime().getMinute(),
        reservation.getGroupSize(),
        new CustomerDto(
            reservation.getCustomer().getName(),
            reservation.getCustomer().getEmail(),
            reservation.getCustomer().getPhoneNumber()));
  }

  public ReservationDto toDto(Reservation reservation, Restaurant restaurant) {
    ReservationDto reservationDto =
        new ReservationDto(
            reservation.getNumber(),
            reservation.getDate(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getEndTime().getMinute() - reservation.getStartTime().getMinute(),
            reservation.getGroupSize(),
            new CustomerDto(
                reservation.getCustomer().getName(),
                reservation.getCustomer().getEmail(),
                reservation.getCustomer().getPhoneNumber()));

    RestaurantDto restaurantDto = new RestaurantAssembler().toDto(restaurant);
    reservationDto.setRestaurantDto(restaurantDto);
    return reservationDto;
  }
}
