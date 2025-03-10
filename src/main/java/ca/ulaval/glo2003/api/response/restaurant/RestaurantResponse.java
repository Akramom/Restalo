package ca.ulaval.glo2003.api.response.restaurant;

import ca.ulaval.glo2003.application.dtos.HoursDto;

public class RestaurantResponse {

  private String id;
  private String name;
  private int capacity;
  private HoursDto hours;

  public RestaurantResponse(String id, String name, int capacity, HoursDto hours) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
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
}
