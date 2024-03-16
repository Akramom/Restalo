package ca.ulaval.glo2003.api.assemblers.response;

import ca.ulaval.glo2003.api.response.reservation.OwnerReservationResponse;
import ca.ulaval.glo2003.api.response.reservation.ReservationResponse;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantResponse;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.dtos.TimeDto;

public class ReservationResponseAssembler {
  public OwnerReservationResponse fromDto(ReservationDto reservationDto) {

    return new OwnerReservationResponse(
        reservationDto.getNumber(),
        reservationDto.getDate(),
        new TimeDto(reservationDto.getStartTime(), reservationDto.getEndTime()),
        reservationDto.getGroupSize(),
        reservationDto.getCustomer());
  }

  public ReservationResponse fromDto(ReservationDto reservationDto, RestaurantDto restaurantDto) {
    return new ReservationResponse(
        reservationDto.getNumber(),
        reservationDto.getDate(),
        new TimeDto(reservationDto.getStartTime(), reservationDto.getEndTime()),
        reservationDto.getGroupSize(),
        reservationDto.getCustomer(),
        new RestaurantResponse(
            restaurantDto.id(),
            restaurantDto.name(),
            restaurantDto.capacity(),
            restaurantDto.hours()));
  }
}
