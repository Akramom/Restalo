package ca.ulaval.glo2003.api.assemblers.request;

import ca.ulaval.glo2003.api.request.RestaurantRequest;
import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.ReservationDurationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;

public class RestaurantRequestAssembler {

  public RestaurantDto toDto(RestaurantRequest restaurantRequest) {

    return new RestaurantDto(
        restaurantRequest.getId(),
        restaurantRequest.getName(),
        restaurantRequest.getCapacity(),
        new HoursDto(
            restaurantRequest.getHours().getOpen(), restaurantRequest.getHours().getClose()),
        restaurantRequest.getReservations() == null
            ? new ReservationDurationDto(60)
            : new ReservationDurationDto(restaurantRequest.getReservations().duration()));
  }
}
