package ca.ulaval.glo2003.entity;

import java.util.UUID;

public class Restaurant {

  private String name;
  private int capacity;
  private String noRestaurant;

  private Hours hours;

  public Restaurant(String name, int capacity, Hours hours) {
    this.noRestaurant = UUID.randomUUID().toString().substring(0, 8);
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
  }

  public void generateId() {
    this.noRestaurant = UUID.randomUUID().toString().substring(0, 8);
  }

  public Restaurant() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public String getNoRestaurant() {
    return noRestaurant;
  }

  public void setNoRestaurant(String noRestaurant) {
    this.noRestaurant = noRestaurant;
  }

  public Hours getHours() {
    return hours;
  }

  public void setHours(Hours hours) {
    this.hours = hours;
  }

  @Override
  public String toString() {
    return "Restaurant{"
        + "name='"
        + name
        + '\''
        + ", capacity="
        + capacity
        + ", noRestaurant="
        + noRestaurant
        + ", hours="
        + hours
        + '}';
  }
}
