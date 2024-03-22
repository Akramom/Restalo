package ca.ulaval.glo2003.api.assemblers.request;

import ca.ulaval.glo2003.api.request.RestaurantRequest;
import ca.ulaval.glo2003.application.dtos.ReservationDurationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;

public class RestaurantRequestAssembler {

  public RestaurantDto toDto(RestaurantRequest restaurantRequest) {

    return new RestaurantDto(
        restaurantRequest.getId(),
        restaurantRequest.getName(),
        restaurantRequest.getCapacity(),
        restaurantRequest.getHours(),
        restaurantRequest.getReservations() == null
            ? new ReservationDurationDto(60)
            : restaurantRequest.getReservations());
  }
}
