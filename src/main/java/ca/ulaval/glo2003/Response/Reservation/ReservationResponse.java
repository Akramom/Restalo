package ca.ulaval.glo2003.Response.Reservation;

import ca.ulaval.glo2003.Response.Restaurant.RestaurantResponse;
import ca.ulaval.glo2003.entity.Customer;
import ca.ulaval.glo2003.entity.Time;
import java.time.LocalDate;

public class ReservationResponse {

  private String number;
  private LocalDate date;
  private Time time;
  private int groupSize;
  private Customer customer;
  private RestaurantResponse restaurant;

  public ReservationResponse(
      String number,
      LocalDate date,
      Time time,
      int groupSize,
      Customer customer,
      RestaurantResponse restaurantResposne) {
    this.number = number;
    this.date = date;
    this.time = time;
    this.groupSize = groupSize;
    this.customer = customer;
    this.restaurant = restaurantResposne;
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

  public RestaurantResponse getRestaurant() {
    return restaurant;
  }
}
