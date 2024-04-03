package ca.ulaval.glo2003.application.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

public class AvailabilityDto {
  private LocalDateTime start;
  private int remainingPlaces;

  private String restaurantId;
  private String id;

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

  public AvailabilityDto(String id, String restaurantId, LocalDateTime start, int remainingPlaces) {
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
