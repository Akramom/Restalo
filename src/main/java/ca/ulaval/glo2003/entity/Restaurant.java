package ca.ulaval.glo2003.entity;

import java.util.UUID;

public class Restaurant {

  private String name;
  private int capacity;
  private String id;

  private ReservationDuration reservationDuration;

  private Hours hours;

  public Restaurant(
      String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = UUID.randomUUID().toString().substring(0, 8);
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservationDuration = reservationDuration;
  }

  public Restaurant(
      String id, String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservationDuration = reservationDuration;
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

  public ReservationDuration getReservation() {
    return this.reservationDuration;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public String getId() {
    return id;
  }

  public Hours getHours() {
    return hours;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setReservationDuration(ReservationDuration reservationDuration) {
    this.reservationDuration = reservationDuration;
  }

  public void setHours(Hours hours) {
    this.hours = hours;
  }

  public void addReservation(Reservation reservation) {}

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
