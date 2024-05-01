package ca.ulaval.glo2003.application.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateReservationDto {
  private LocalDate date;
  private LocalTime startTime;

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public int getGroupSize() {
    return groupSize;
  }

  private LocalTime endTime;
  private int groupSize;

  public UpdateReservationDto(LocalDate date, LocalTime startTime, int groupSize) {
    this.date = date;
    this.startTime = startTime;
    this.groupSize = groupSize;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public void setGroupSize(int groupSize) {
    this.groupSize = groupSize;
  }
}
