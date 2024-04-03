package ca.ulaval.glo2003.application.validator;

import ca.ulaval.glo2003.application.dtos.CustomerDto;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.util.Util;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class ReservationValidator {
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10}$");

  public void validateReservationToRestaurant(
      ReservationDto reservationDto,
      LocalTime restaurantOpeningTime,
      LocalTime restaurantClosingTime,
      int maxCapacity)
      throws InvalidParameterException {
    validateReservationTimeForRestaurant(
        reservationDto, restaurantOpeningTime, restaurantClosingTime);
    validateGroupSize(reservationDto.getGroupSize(), maxCapacity);
    validateCustomer(reservationDto.getCustomer());
  }

  public void validateReservationTimeForRestaurant(
      ReservationDto reservationDto,
      LocalTime restaurantOpeningTime,
      LocalTime restaurantClosingTime)
      throws InvalidParameterException {

    validateAdjustedStartingTime(reservationDto.getStartTime(), restaurantOpeningTime);
    validateStartingTimeBeforeClosingTime(reservationDto.getStartTime(), restaurantClosingTime);
    validateEndingTimeBeforeClosingTime(reservationDto.getEndTime(), restaurantClosingTime);
  }

  private void validateStartingTimeBeforeClosingTime(LocalTime startTime, LocalTime closingTime)
      throws InvalidParameterException {
    if (!startTime.isBefore(closingTime)) {
      throw new InvalidParameterException(
          "The reservation must start before the restaurant's closing time");
    }
  }

  private void validateEndingTimeBeforeClosingTime(LocalTime endTime, LocalTime closingTime)
      throws InvalidParameterException {
    if (endTime.isAfter(closingTime)) {
      throw new InvalidParameterException(
          "The reservation must end before or at the restaurant's closing time");
    }
  }

  private void validateAdjustedStartingTime(LocalTime startTime, LocalTime openingTime)
      throws InvalidParameterException {
    LocalTime adjustedStartTime = Util.ajustStartTimeToNext15Min(startTime);
    if (adjustedStartTime.isBefore(openingTime)) {
      throw new InvalidParameterException(
          "The adjusted starting time of the reservation can't be before the restaurant's opening time");
    }
  }

  private void validateGroupSize(int groupSize, int maxCapacity) throws InvalidParameterException {
    if (groupSize <= 0) {
      throw new InvalidParameterException("Group size can not be equal or lower than 0");
    }
    if (groupSize > maxCapacity) {
      throw new InvalidParameterException(
          "The group size exceeds the maximum capacity of the restaurant for this moment");
    }
  }

  private void validateCustomer(CustomerDto customer) throws InvalidParameterException {
    String phoneNumber = customer.getPhoneNumber();
    String email = customer.getEmail();

    validatePhoneNumber(phoneNumber);
    validateEmail(email);
  }

  private void validatePhoneNumber(String phoneNumber) throws InvalidParameterException {
    if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
      throw new InvalidParameterException("Invalid Phone number");
    }
  }

  private void validateEmail(String email) throws InvalidParameterException {
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new InvalidParameterException("Invalid Email.");
    }
  }
}
