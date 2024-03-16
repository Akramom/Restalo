package ca.ulaval.glo2003.application.assembler;

import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.ReservationDurationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.domain.entity.Hours;
import ca.ulaval.glo2003.domain.entity.ReservationDuration;
import ca.ulaval.glo2003.domain.entity.Restaurant;

public class RestaurantAssembler {

  public RestaurantAssembler() {}

  public RestaurantDto toDto(Restaurant restaurant) {
    return new RestaurantDto(
        restaurant.getId(),
        restaurant.getName(),
        restaurant.getCapacity(),
        restaurant.getHours() == null
            ? null
            : new HoursDto(restaurant.getHours().getOpen(), restaurant.getHours().getClose()),
        restaurant.getReservation() == null
            ? null
            : new ReservationDurationDto(restaurant.getReservation().duration()));
  }

  public Restaurant fromDto(RestaurantDto restaurantDto) {
    return new Restaurant(
        restaurantDto.id(),
        restaurantDto.name(),
        restaurantDto.capacity(),
        new Hours(restaurantDto.hours().getOpen(), restaurantDto.hours().getClose()),
        new ReservationDuration(restaurantDto.reservations().duration()));
  }
}
