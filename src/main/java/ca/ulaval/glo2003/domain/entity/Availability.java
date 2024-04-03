package ca.ulaval.glo2003.domain.entity;

import ca.ulaval.glo2003.util.Util;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Availability {
  private LocalDateTime start;
  private int remainingPlaces;

  private String restaurantId;
  @Id private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(String restaurantId) {
    this.restaurantId = restaurantId;
  }

  public Availability(LocalDateTime start, int remainingPlaces) {
    id = Util.generateId();
    this.remainingPlaces = remainingPlaces;
    this.start = start;
  }

  public Availability(String id, String restaurantId, LocalDateTime start, int remainingPlaces) {
    this.id = id;
    this.restaurantId = restaurantId;
    this.remainingPlaces = remainingPlaces;
    this.start = start;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public int getRemainingPlaces() {
    return remainingPlaces;
  }

  public void setRemainingPlaces(int remainingPlaces) {
    this.remainingPlaces = remainingPlaces;
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, remainingPlaces);
  }
}
