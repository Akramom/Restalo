package ca.ulaval.glo2003.api.response.reservation;

import ca.ulaval.glo2003.application.dtos.CustomerDto;
import ca.ulaval.glo2003.application.dtos.TimeDto;
import java.time.LocalDate;

public class OwnerReservationResponse {

  private String number;
  private LocalDate date;
  private TimeDto time;
  private int groupSize;
  private CustomerDto customer;

  public OwnerReservationResponse(
      String number, LocalDate date, TimeDto time, int groupSize, CustomerDto customer) {
    this.number = number;
    this.date = date;
    this.time = time;
    this.groupSize = groupSize;
    this.customer = customer;
  }

  public String getNumber() {
    return number;
  }

  public LocalDate getDate() {
    return date;
  }

  public TimeDto getTime() {
    return time;
  }

  public int getGroupSize() {
    return groupSize;
  }

  public CustomerDto getCustomer() {
    return customer;
  }
}
