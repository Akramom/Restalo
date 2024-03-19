package ca.ulaval.glo2003.api.response.restaurant;

import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.ReservationDurationDto;

public class OwnerRestaurantResponse {
  private ReservationDurationDto reservations;
  private String id;
  private String name;
  private int capacity;
  private HoursDto hours;

  public OwnerRestaurantResponse(
      String id, String name, int capacity, HoursDto hours, ReservationDurationDto reservations) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservations = reservations;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getCapacity() {
    return capacity;
  }

  public HoursDto getHours() {
    return hours;
  }

  public ReservationDurationDto getReservations() {
    return reservations;
  }
}
