package ca.ulaval.glo2003.api.assemblers.request;

import static ca.ulaval.glo2003.util.Constante.DEFAULT_DURATION;

import ca.ulaval.glo2003.api.request.RestaurantRequest;
import ca.ulaval.glo2003.api.request.UpdateRestaurantRequest;
import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.ReservationDurationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.dtos.UpdateRestaurantDto;

public class RestaurantRequestAssembler {

  public RestaurantDto toDto(RestaurantRequest restaurantRequest) {

    return new RestaurantDto(
        restaurantRequest.getId(),
        restaurantRequest.getName(),
        restaurantRequest.getCapacity(),
        new HoursDto(
            restaurantRequest.getHours().getOpen(), restaurantRequest.getHours().getClose()),
        restaurantRequest.getReservations() == null
            ? new ReservationDurationDto(DEFAULT_DURATION)
            : new ReservationDurationDto(restaurantRequest.getReservations().duration()));
  }

  public UpdateRestaurantDto toDto(UpdateRestaurantRequest updateRestaurantRequest) {

    return new UpdateRestaurantDto(
        updateRestaurantRequest.name,
        updateRestaurantRequest.capacity,
        updateRestaurantRequest.hours == null
            ? null
            : new HoursDto(
                updateRestaurantRequest.hours.getOpen(), updateRestaurantRequest.hours.getClose()),
        updateRestaurantRequest.reservations == null
            ? null
            : new ReservationDurationDto(updateRestaurantRequest.reservations.duration()));
  }
}
