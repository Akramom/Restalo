package ca.ulaval.glo2003.api.response.reservation;

import ca.ulaval.glo2003.api.response.restaurant.RestaurantPartialResponse;
import ca.ulaval.glo2003.domain.entity.Customer;
import ca.ulaval.glo2003.domain.entity.Time;
import java.time.LocalDate;

public class ReservationResponse extends ReservationPartialResponse {

  private RestaurantPartialResponse restaurantPartialResponse;

  public ReservationResponse(
      String number,
      LocalDate date,
      Time time,
      int groupSize,
      Customer customer,
      RestaurantPartialResponse restaurantPartialResponse) {
    super(number, date, time, groupSize, customer);
    this.restaurantPartialResponse = restaurantPartialResponse;
  }
}
