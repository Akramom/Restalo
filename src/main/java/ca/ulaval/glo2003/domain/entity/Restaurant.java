package ca.ulaval.glo2003.domain.entity;

import ca.ulaval.glo2003.util.Util;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Restaurant {

  private String name;
  private int capacity;
  @Id private String id;
  private ReservationDuration reservationDuration;
  private Hours hours;

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  private String ownerId;
  private List<Reservation> reservationList;

  public Restaurant(
      String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = Util.generateId();
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    if (reservationDuration != null) this.reservationDuration = reservationDuration;
    else this.reservationDuration = new ReservationDuration(60);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Restaurant that)) return false;
    return capacity == that.capacity
        && Objects.equals(name, that.name)
        && Objects.equals(id, that.id)
        && Objects.equals(reservationDuration, that.reservationDuration)
        && Objects.equals(hours, that.hours)
        && Objects.equals(ownerId, that.ownerId)
        && Objects.equals(reservationList, that.reservationList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, capacity, id, reservationDuration, hours, ownerId, reservationList);
  }

  public Restaurant(
      String id, String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
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
    if (reservationList == null) reservationList = new ArrayList<>();
    reservationList.add(reservation);
  }

  public List<Reservation> getReservationList() {
    if (reservationList == null) reservationList = new ArrayList<>();
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
