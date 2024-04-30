package ca.ulaval.glo2003.domain.entity;

import static ca.ulaval.glo2003.util.Constante.DEFAULT_DURATION;

import ca.ulaval.glo2003.util.Constante;
import ca.ulaval.glo2003.util.Util;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private String ownerId;
  private List<Reservation> reservationList;
  private ArrayList<Availability> availabilities;

  public Restaurant() {}

  public Restaurant(
      String id, String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    if (reservationDuration != null) this.reservationDuration = reservationDuration;
    else this.reservationDuration = new ReservationDuration(DEFAULT_DURATION);
    availabilities = new ArrayList<>();
  }

  public Restaurant(
      String name, int capacity, Hours hours, ReservationDuration reservationDuration) {
    this.id = Util.generateId();
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    if (reservationDuration != null) this.reservationDuration = reservationDuration;
    else this.reservationDuration = new ReservationDuration(DEFAULT_DURATION);
    availabilities = new ArrayList<>();
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public List<Availability> getAvailabilities() {
    if (availabilities == null) availabilities = new ArrayList<>();
    return availabilities;
  }

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

  public void setId(String id) {
    this.id = id;
    if (availabilities == null) availabilities = new ArrayList<>();
  }

  public Hours getHours() {
    return hours;
  }

  public void setReservations(ReservationDuration reservationDuration) {
    this.reservationDuration = reservationDuration;
  }

  public void setHours(Hours hours) {
    this.hours = hours;
  }

  public List<Reservation> getReservationList() {
    if (reservationList == null) reservationList = new ArrayList<>();
    return reservationList;
  }

  public void generateId() {
    this.id = UUID.randomUUID().toString().substring(0, 8);
  }

  public void setAvailability(Availability updatedAvailability) {
    availabilities.stream()
        .filter(availability -> availability.getId().equals(updatedAvailability.getId()))
        .findFirst()
        .ifPresent(
            availability ->
                availability.setRemainingPlaces(updatedAvailability.getRemainingPlaces()));
  }

  public void addAvailabilities(LocalDate dateAvailability) {
    if (availabilities == null) availabilities = new ArrayList<>();

    LocalTime startTimeAvailability = ajustStartTime(hours.getOpen());
    LocalTime endTimeAvailability = calculEndTime(hours.getClose());

    while (startTimeAvailability.isBefore(endTimeAvailability)
        || startTimeAvailability.equals(endTimeAvailability)) {
      addAvailability(dateAvailability, startTimeAvailability, capacity);
      startTimeAvailability = startTimeAvailability.plusMinutes(Constante.AJUST_TIME);
    }
  }

  public void removeReservation(String reservationNumber) {
    getReservationList().removeIf(reservation -> reservation.getNumber().equals(reservationNumber));
  }

  public void addReservation(Reservation reservation) {
    if (reservationList == null) reservationList = new ArrayList<>();
    reservationList.add(reservation);
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

  private void addAvailability(
      LocalDate dateAvailability, LocalTime startTimeAvailability, int capacity) {
    LocalDateTime start = LocalDateTime.of(dateAvailability, startTimeAvailability);
    Availability availability = new Availability(start, capacity);
    availability.setRestaurantId(id);
    availabilities.add(availability);
  }

  private LocalTime calculEndTime(LocalTime close) {
    return Util.adjustToPrevious15Minutes(close)
        .minusMinutes(
            reservationDuration == null ? DEFAULT_DURATION : reservationDuration.duration());
  }

  private LocalTime ajustStartTime(LocalTime open) {
    return Util.ajustStartTimeToNext15Min(open);
  }
}
