package ca.ulaval.glo2003.api.request;

import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.ReservationDurationDto;
import ca.ulaval.glo2003.util.Constante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RestaurantRequest {

  public String id;

  @NotNull(message = Constante.MISSING_RESTAURANT_PARAMETER)
  @NotEmpty(message = Constante.MISSING_RESTAURANT_PARAMETER)
  public String name;

  public int capacity;

  @Valid
  @NotNull(message = Constante.MISSING_RESTAURANT_PARAMETER)
  public HoursDto hours;

  public ReservationDurationDto reservations;

  public RestaurantRequest() {}

  public RestaurantRequest(
      String id,
      String name,
      int capacity,
      HoursDto hours,
      ReservationDurationDto reservationDurationDto) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservations = reservationDurationDto;
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

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public void setHours(HoursDto hours) {
    this.hours = hours;
  }
}
