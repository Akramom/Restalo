package ca.ulaval.glo2003.domain.entity;

import ca.ulaval.glo2003.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Restaurant {

  private String name;
  private int capacity;
  private String id;
  private ReservationDuration reservationDuration;
  private Hours hours;
  private List<Reservation> reservationList;

  public Restaurant(
      String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = Util.generateId();
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservationList = new ArrayList<>();
    if (reservationDuration != null) this.reservationDuration = reservationDuration;
    else this.reservationDuration = new ReservationDuration(60);
  }

  public Restaurant(
      String id, String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservationList = new ArrayList<>();
    if (reservationDuration != null) this.reservationDuration = reservationDuration;
    else this.reservationDuration = new ReservationDuration(60);
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

  public void setReservations(ReservationDuration reservationDuration) {
    this.reservationDuration = reservationDuration;
  }

  public void setHours(Hours hours) {
    this.hours = hours;
  }

  public void addReservation(Reservation reservation) {
    reservationList.add(reservation);
  }

  public List<Reservation> getReservationList() {
    return reservationList;
  }

  @Override
  public String toString() {
    return "Restaurant{"
        + "name='"
        + name
        + '\''
        + ", capacity="
        + capacity
        + ", number='"
        + id
        + '\''
        + ", reservationDuration="
        + reservationDuration
        + ", hours="
        + hours
        + ", reservations="
        + reservationList
        + '}';
  }
}
