package ca.ulaval.glo2003.api.response.restaurant;

import ca.ulaval.glo2003.domain.entity.Hours;

public class RestaurantPartialResponse {

  private String id;
  private String name;
  private int capacity;
  private Hours hours;

  public RestaurantPartialResponse(String id, String name, int capacity, Hours hours) {
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

  public Hours getHours() {
    return hours;
  }
}
