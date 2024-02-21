package ca.ulaval.glo2003.util;

import ca.ulaval.glo2003.entity.*;
import java.time.LocalTime;

public class Validator {
  public Validator() {}

  public Boolean isRestaurantParameterEmpty(Restaurant restaurant) {
    String name = restaurant.getName();
    Hours hours = restaurant.getHours();

    if (isStringEmpty(name)) {
      return true;
    }

    if (hours == null || hours.getOpen() == null || hours.getClose() == null) {
      return true;
    }

    return false;
  }

  public Boolean isValidCapacity(int capacity) {
    return capacity > 0;
  }

  public Boolean isValidOpeningHours(Hours openingHours) {
    LocalTime openTime = openingHours.getOpen();
    LocalTime closeTime = openingHours.getClose();

    int timeOpen = closeTime.getHour() - openTime.getHour();

    return timeOpen >= 1;
  }

  public Boolean isValidRestaurant(Restaurant restaurant) {
    if (!isValidOpeningHours(restaurant.getHours())) {
      return false;
    }
    if (!isValidCapacity(restaurant.getCapacity())) {
      return false;
    }
    return true;
  }

  public Boolean isStringEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }
}
