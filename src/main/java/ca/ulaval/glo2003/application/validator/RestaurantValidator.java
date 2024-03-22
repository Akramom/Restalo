package ca.ulaval.glo2003.application.validator;

import static ca.ulaval.glo2003.util.Constante.INVALID_RESTAURANT_PARAMETER;

import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import java.time.LocalTime;

public class RestaurantValidator {
  public RestaurantValidator() {}

  public void validRestaurant(RestaurantDto restaurant) throws InvalidParameterException {
    if (!isValidOpeningHours(restaurant.hours())) {
      throw new InvalidParameterException(INVALID_RESTAURANT_PARAMETER);
    }
    if (!isValidCapacity(restaurant.capacity())) {
      throw new InvalidParameterException(INVALID_RESTAURANT_PARAMETER);
    }
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
}
