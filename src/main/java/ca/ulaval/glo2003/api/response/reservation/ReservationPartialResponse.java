package ca.ulaval.glo2003.api.response.reservation;

import ca.ulaval.glo2003.domain.entity.Customer;
import ca.ulaval.glo2003.domain.entity.Time;
import java.time.LocalDate;

public class ReservationPartialResponse {

  private String number;
  private LocalDate date;
  private Time time;
  private int groupSize;
  private Customer customer;

  public ReservationPartialResponse(
      String number, LocalDate date, Time time, int groupSize, Customer customer) {
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

  public Time getTime() {
    return time;
  }

  public int getGroupSize() {
    return groupSize;
  }

  public Customer getCustomer() {
    return customer;
  }
}
