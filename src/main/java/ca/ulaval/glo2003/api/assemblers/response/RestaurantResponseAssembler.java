package ca.ulaval.glo2003.api.assemblers.response;

import ca.ulaval.glo2003.api.response.restaurant.OwnerRestaurantResponse;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantResponse;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;

public class RestaurantResponseAssembler {
  public OwnerRestaurantResponse ownerRestaurantResponseFromDto(RestaurantDto restaurantDto) {
    return new OwnerRestaurantResponse(
        restaurantDto.id(),
        restaurantDto.name(),
        restaurantDto.capacity(),
        restaurantDto.hours(),
        restaurantDto.reservations());
  }

  public RestaurantResponse restaurantResponseFromDto(RestaurantDto restaurantDto) {
    return new RestaurantResponse(
        restaurantDto.id(), restaurantDto.name(), restaurantDto.capacity(), restaurantDto.hours());
  }
}
