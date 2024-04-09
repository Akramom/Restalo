package ca.ulaval.glo2003.util;

public class Constante {
  public static final String MISSING_OWNER_ID = "Missing owner ID.";
  public static final String MISSING_RESTAURANT_PARAMETER = "Missing restaurant parameter.";
  public static final String MISSING_RESTAURANT_ID = "Missing restaurant ID.";
  public static final String MISSING_CUSTOMER_PARAMETER = "Missing customer parameter.";
  public static final String INVALID_RESTAURANT_PARAMETER = "Invalid restaurant parameter.";
  public static final String RESERVATION_NOT_FOUND = "reservation not found.";
  public static final String RESTAURANT_NOT_FOUND = "restaurant not found.";
  public static final String OWNER_NOT_FOUND = "owner not found.";
  public static final String ERROR_DESERIALIZE =
      "Invalid restaurant parameter. The restaurant could not be deserialize";
  public static final String MISSING_DATE = "missing date parameter";
  public static final String INVALID_DATE = "Invalid  date format";
  public static final String NUMBER_OF_PLACES_UNAVAILABLE =
      "group size is greater than the number of places available";
  public static final String SERVICE_IS_HEALTHY = "Service is healthy";
  public static final int DEFAULT_DURATION = 60;
  public static final String UNEXPECTED_ERROR =
      "An unexpected error occurred. Please review your request.";

  public static final String AVAILABILITY_NOT_FOUND = "Availability not found for the given date";

  public static final String MISSING_RESERVATION_DATE = "Missing reservation date.";
  public static final String MISSING_RESERVATION_START_TIME = "Missing reservation start time.";
  public static final String MISSING_CUSTOMER_IN_RESERVATION = "Missing customer in reservation.";
  public static final String URL = "http://0.0.0.0";
  public static final String PATTERN_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
  public static final String PATTERN_PHONE = "^\\+?\\d{10}$";
  public static String PORT = "8080";
}
