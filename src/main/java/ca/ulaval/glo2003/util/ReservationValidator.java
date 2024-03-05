package ca.ulaval.glo2003.util;

import ca.ulaval.glo2003.entity.Customer;
import ca.ulaval.glo2003.entity.Reservation;
import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.exception.MissingParameterException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class ReservationValidator {
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10}$");

  public void isEmptyReservationParameter(Reservation reservation)
      throws MissingParameterException {
    LocalDate date = reservation.getDate();
    LocalTime startTime = reservation.getStartTime();
    Customer customer = reservation.getCustomer();
    if (date == null) {
      throw new MissingParameterException("Missing reservation date.");
    }
    if (startTime == null) {
      throw new MissingParameterException("Missing reservation start time.");
    }
    isEmptyCustomer(customer);
  }

  public void isEmptyCustomer(Customer customer) throws MissingParameterException {
    if (customer == null) {
      throw new MissingParameterException("Missing customer in reservation.");
    }
    if (isStringEmpty(customer.getName())) {
      throw new MissingParameterException("Missing customer name in reservation.");
    }
    if (isStringEmpty(customer.getEmail())) {
      throw new MissingParameterException("Missing customer email in reservation.");
    }
    if (isStringEmpty(customer.getPhoneNumber())) {
      throw new MissingParameterException("Missing customer phone number in reservation.");
    }
  }

  public Boolean isStringEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  public void validateReservationToRestaurant(
      Reservation reservation, LocalTime restaurantClosingTime) throws InvalidParameterException {
    validateReservationTimeForRestaurant(reservation, restaurantClosingTime);
    validateGroupSize(reservation.getGroupSize());
    validateCustomer(reservation.getCustomer());
  }

  private void validateReservationTimeForRestaurant(
      Reservation reservation, LocalTime restaurantClosingTime) throws InvalidParameterException {
    LocalTime closingTime = restaurantClosingTime;

    LocalTime startTime = reservation.getStartTime();
    LocalTime endTime = reservation.getEndTime();

    validateStartingTime(startTime.compareTo(closingTime));
    validateEndingTime(endTime.compareTo(closingTime));
  }

  public void validateStartingTime(int startCompareToCloseTime) throws InvalidParameterException {
    if (startCompareToCloseTime >= 0) {
      throw new InvalidParameterException(
          "The start of the reservation can not be during or after the restaurant's closing time");
    }
  }

  private void validateEndingTime(int endCompareToCloseTime) throws InvalidParameterException {
    if (endCompareToCloseTime > 0) {
      throw new InvalidParameterException(
          "The reservation can not end after the restaurant's closing time");
    }
  }

  private void validateGroupSize(int groupSize) throws InvalidParameterException {
    if (groupSize <= 0) {
      throw new InvalidParameterException("Group size can not be equal or lower than 0");
    }
  }

  private void validateCustomer(Customer customer) throws InvalidParameterException {
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
