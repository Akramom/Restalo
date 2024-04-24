package ca.ulaval.glo2003.api.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateReservationRequest {

  private LocalDate date;
  private LocalTime startTime;
  private int groupSize;

  public UpdateReservationRequest() {}

  public UpdateReservationRequest(LocalDate date, LocalTime startTime, int groupSize) {
    this.date = date;
    this.startTime = startTime;
    this.groupSize = groupSize;
  }

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public int getGroupSize() {
    return groupSize;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public void setGroupSize(int groupSize) {
    this.groupSize = groupSize;
  }
}
