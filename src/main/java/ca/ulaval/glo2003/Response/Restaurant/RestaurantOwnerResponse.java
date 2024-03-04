package ca.ulaval.glo2003.Response.Restaurant;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.ReservationDuration;

public class RestaurantOwnerResponse extends RestaurantResponse {
  private ReservationDuration reservations;

  public RestaurantOwnerResponse(
      String id, String name, int capacity, Hours hours, ReservationDuration reservations) {
    super(id, name, capacity, hours);
    this.reservations = reservations;
  }

  public ReservationDuration getReservations() {
    return reservations;
  }
}
