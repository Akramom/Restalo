package ca.ulaval.glo2003.application.validator;

import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import java.time.LocalTime;

public class RestaurantValidator {
  public RestaurantValidator() {}

  public Boolean isRestaurantParameterEmpty(RestaurantDto restaurant) {
    String name = restaurant.name();
    HoursDto hours = restaurant.hours();

    if (isStringEmpty(name)) {
      return true;
    }

    if (hours == null || hours.getOpen() == null || hours.getClose() == null) {
      return true;
    }

    return false;
  }

  public Boolean isStringEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  public Boolean isValidCapacity(int capacity) {
    return capacity > 0;
  }

  public Boolean isValidOpeningHours(HoursDto openingHours) {
    LocalTime openTime = openingHours.getOpen();
    LocalTime closeTime = openingHours.getClose();

    int timeOpen = closeTime.getHour() - openTime.getHour();

    return timeOpen >= 1;
  }

  public Boolean isValidRestaurant(RestaurantDto restaurant) {
    if (!isValidOpeningHours(restaurant.hours())) {
      return false;
    }
    if (!isValidCapacity(restaurant.capacity())) {
      return false;
    }
    return true;
  }
}
