package ca.ulaval.glo2003.api.request;

import ca.ulaval.glo2003.application.dtos.CustomerDto;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequest {
  private int durationInMin;
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;
  private int groupSize;
  private CustomerDto customer;
  private String number;

  public ReservationRequest(
      String number,
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      int duration,
      int groupSize,
      CustomerDto customer) {
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.durationInMin = duration;
    this.groupSize = groupSize;
    this.customer = customer;
    this.number = number;
  }

  public int getDurationInMin() {
    return durationInMin;
  }

  public void setDurationInMin(int durationInMin) {
    this.durationInMin = durationInMin;
  }

  public ReservationRequest() {}

  public LocalDate getDate() {
    return date;
  }

  public String getNumber() {
    return number;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public int getGroupSize() {
    return groupSize;
  }

  public CustomerDto getCustomer() {
    return customer;
  }

  public LocalTime getEndTime() {
    return endTime;
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

  public void setCustomer(CustomerDto customer) {
    this.customer = customer;
  }

  public void setNumber(String number) {
    this.number = number;
  }
}
