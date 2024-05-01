package ca.ulaval.glo2003.api.request;

public class UpdateRestaurantRequest {

  public String name;
  public int capacity;
  public HoursRequest hours;
  public ReservationDurationRequest reservations;

  public UpdateRestaurantRequest() {}

  public UpdateRestaurantRequest(
      String name, int capacity, HoursRequest hours, ReservationDurationRequest reservations) {
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservations = reservations;
  }
}
