package ca.ulaval.glo2003.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Reservation {
  public LocalDate date;
  public LocalTime startTime;
  public LocalTime endTime;
  public int durationInMin;
  public int groupSize;
  public Customer customer;
  private String id;

  public Reservation(
      LocalDate date, LocalTime startTime, int durationInMin, int groupSize, Customer customer) {
    this.date = date;
    this.startTime = addToNext15MinSlot(startTime);
    setDurationInMin(durationInMin);
    this.groupSize = groupSize;
    this.customer = customer;
    this.id = UUID.randomUUID().toString();
  }

  public LocalTime addToNext15MinSlot(LocalTime time) {
    int minutes = time.getMinute();
    int minutesOverThePrevious15MinSlot = minutes % 15;
    if (minutesOverThePrevious15MinSlot == 0) {
      return time;
    }
    return time.plusMinutes(15 - minutesOverThePrevious15MinSlot);
  }

  public Reservation() {
    setNewID();
  }

  public String setNewID() {
    return this.id = UUID.randomUUID().toString();
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
    this.startTime = addToNext15MinSlot(startTime);
  }

  public void setDurationInMin(int durationInMin) {
    this.durationInMin = durationInMin;
    setEndTime();
  }

  private void setEndTime() {
    this.endTime = this.startTime.plusMinutes(this.durationInMin);
  }

  public void setGroupSize(int groupSize) {
    this.groupSize = groupSize;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  @Override
  public String toString() {
    return "{ "
        + "date :"
        + date
        + ", startTime :"
        + startTime
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
