package ca.ulaval.glo2003.domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;
  private int groupSize;
  private Customer customer;
  private String number;

  public Reservation(
      String number,
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      int groupSize,
      Customer customer) {
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.groupSize = groupSize;
    this.customer = customer;
    this.number = number;
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

  public Customer getCustomer() {
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

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return "{ "
        + "date :"
        + date
        + ", startTime :"
        + startTime
        + ", endTime :"
        + endTime
        + ", groupSize :"
        + groupSize
        + ", customer : {"
        + "name: "
        + customer.getName()
        + ", email: "
        + customer.getEmail()
        + ", phoneNumber :"
        + customer.getPhoneNumber()
        + '}'
        + '}';
  }
}
