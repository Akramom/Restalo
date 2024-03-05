package ca.ulaval.glo2003.api.response.restaurant;

import ca.ulaval.glo2003.domain.entity.Hours;
import ca.ulaval.glo2003.domain.entity.ReservationDuration;

public class RestaurantResponse extends RestaurantPartialResponse {
  private ReservationDuration reservations;

  public RestaurantResponse(
      String id, String name, int capacity, Hours hours, ReservationDuration reservations) {
    super(id, name, capacity, hours);
    this.reservations = reservations;
  }

  public ReservationDuration getReservations() {
    return reservations;
  }
}
