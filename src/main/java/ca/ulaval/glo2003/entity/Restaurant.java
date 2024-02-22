package ca.ulaval.glo2003.entity;

import java.util.UUID;

public class Restaurant {

  private String name;
  private int capacity;
  private String id;

  private ReservationsRequest reservations;
  private Hours hours;

  public Restaurant(String name, int capacity, Hours hours, ReservationsRequest reservation) {
    this.id = UUID.randomUUID().toString().substring(0, 8);
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    if (reservation == null) {
      this.reservations = new ReservationsRequest(60);
    } else this.reservations = reservation;
  }

  public void generateId() {
    this.id = UUID.randomUUID().toString().substring(0, 8);
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

  public ReservationsRequest getReservations() {
    return this.reservations;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Hours getHours() {
    return hours;
  }

  public void setReservations(ReservationsRequest reservations) {
    this.reservations = reservations;
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
        + ", id="
        + id
        + ", hours="
        + hours
        + '}'
        + "duration:";
  }
}
