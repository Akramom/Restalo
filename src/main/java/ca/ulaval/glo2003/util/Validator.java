package ca.ulaval.glo2003.util;

import ca.ulaval.glo2003.entity.*;
import java.time.LocalTime;

public class Validator {
  public Validator() {}

  public Boolean emptyRestaurantParameter(Restaurant restaurant) {
    String name = restaurant.getName();
    if (checkStringEmpty(name)) {
      return true;
    }

    Hours hours = restaurant.getHours();
    if (hours == null) {
      return true;
    }

    LocalTime openTime = hours.getOpen();
    LocalTime closeTime = hours.getClose();
    if (openTime == null || closeTime == null) {
      return true;
    }

    return false;
  }

  public Boolean validCapacity(int capacity) {
    return capacity > 0;
  }

  public Boolean validOpeningHours(Hours openingHours) {
    LocalTime openTime = openingHours.getOpen();
    LocalTime closeTime = openingHours.getClose();

    int timeOpen = closeTime.getHour() - openTime.getHour();

    return timeOpen >= 1;
  }

  public Boolean validRestaurant(Restaurant restaurant) {
    if (!validOpeningHours(restaurant.getHours())) {
      return false;
    }
    if (!validCapacity(restaurant.getCapacity())) {
      return false;
    }
    return true;
  }

  public Boolean checkStringEmpty(String value) {
    return value == null || value.isEmpty();
  }
}
