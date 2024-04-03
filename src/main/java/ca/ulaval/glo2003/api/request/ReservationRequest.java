package ca.ulaval.glo2003.api.request;

import ca.ulaval.glo2003.util.Constante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequest {
  private int durationInMin;

  @NotNull(message = Constante.MISSING_RESERVATION_DATE)
  private LocalDate date;

  @NotNull(message = Constante.MISSING_RESERVATION_START_TIME)
  private LocalTime startTime;

  private LocalTime endTime;
  private int groupSize;

  @Valid
  @NotNull(message = Constante.MISSING_CUSTOMER_IN_RESERVATION)
  private CustomerRequest customer;

  private String number;

  public ReservationRequest() {}

  public ReservationRequest(
      String number,
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      int duration,
      int groupSize,
      CustomerRequest customer) {
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

  public CustomerRequest getCustomer() {
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

  public void setCustomer(CustomerRequest customer) {
    this.customer = customer;
  }

  public void setNumber(String number) {
    this.number = number;
  }
}
