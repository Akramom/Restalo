package ca.ulaval.glo2003.entity;

import ca.ulaval.glo2003.util.Util;
import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;
  private int groupSize;
  private Customer customer;
  private String id;

  public Reservation(
      LocalDate date, LocalTime startTime, int durationInMin, int groupSize, Customer customer) {
    this.date = date;
    this.startTime = addToNext15MinSlot(startTime);
    this.setEndTime(durationInMin);
    this.groupSize = groupSize;
    this.customer = customer;
    this.id = Util.generateId();
  }

  public Reservation(
      String id,
      LocalDate date,
      LocalTime startTime,
      int durationInMin,
      int groupSize,
      Customer customer) {
    this.date = date;
    this.startTime = addToNext15MinSlot(startTime);
    this.setEndTime(durationInMin);
    this.groupSize = groupSize;
    this.customer = customer;
    this.id = id;
  }

  public LocalTime addToNext15MinSlot(LocalTime time) {
    int minutes = time.getMinute();
    int minutesOverThePrevious15MinSlot = minutes % 15;
    if (minutesOverThePrevious15MinSlot == 0) {
      return time;
    }
    return time.plusMinutes(15 - minutesOverThePrevious15MinSlot);
  }

  public void ajustStartTimeToNext15Min() {
    this.startTime = addToNext15MinSlot(startTime);
  }

  public Reservation() {
    setNewID();
  }

  public String setNewID() {
    return this.id = Util.generateId();
  }

  public LocalDate getDate() {
    return date;
  }

  public String getId() {
    return id;
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

  public void setId(String id) {
    if (id != null) setNewID();
    else this.id = id;
  }

  public void setEndTime(int durationInMin) {
    this.endTime = this.startTime.plusMinutes(durationInMin);
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
