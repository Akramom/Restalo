package ca.ulaval.glo2003.application.assembler;

import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.domain.entity.Restaurant;

public class RestaurantAssembler {

  public RestaurantAssembler() {}

  public RestaurantDto toDto(Restaurant restaurant) {
    return new RestaurantDto(
        restaurant.getId(),
        restaurant.getName(),
        restaurant.getCapacity(),
        restaurant.getHours(),
        restaurant.getReservation());
  }

  public Restaurant fromDto(RestaurantDto restaurantDto) {
    return new Restaurant(
        restaurantDto.id(),
        restaurantDto.name(),
        restaurantDto.capacity(),
        restaurantDto.hours(),
        restaurantDto.reservations());
  }
}
