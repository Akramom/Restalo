package ca.ulaval.glo2003.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.entity.Customer;
import ca.ulaval.glo2003.entity.Reservation;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.exception.MissingParameterException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReservationValidatorTest {
  private final LocalDate DATE = LocalDate.of(12, 12, 12);
  private final LocalTime START_TIME = LocalTime.of(13, 13, 13);
  private final int GROUP_SIZE = 2;
  private final String CUSTOMER_NAME = "Jane Doe";
  private final String EMAIL = "test@test.com";
  private final String PHONE_NUMBER = "1111111111";
  private final int RESERVATION_DURATION = 60;
  private final LocalTime RESTAURANT_CLOSE_TIME = LocalTime.of(20, 20, 20);
  public ReservationValidator validator;
  @Mock private Reservation reservation;
  @Mock private Customer customer;
  @Mock private Restaurant restaurant;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    when(reservation.getDate()).thenReturn(DATE);
    when(reservation.getStartTime()).thenReturn(START_TIME);
    when(reservation.getEndTime()).thenReturn(START_TIME.plusMinutes(RESERVATION_DURATION));
    when(reservation.getGroupSize()).thenReturn(GROUP_SIZE);
    when(reservation.getCustomer()).thenReturn(customer);
    when(reservation.getCustomer().getName()).thenReturn(CUSTOMER_NAME);
    when(reservation.getCustomer().getEmail()).thenReturn(EMAIL);
    when(reservation.getCustomer().getPhoneNumber()).thenReturn(PHONE_NUMBER);
    validator = new ReservationValidator();
  }

  @Test
  void whenReservationParametersNotNullOrEmpty_thenShouldNotThrow() {
    assertDoesNotThrow(() -> validator.isEmptyReservationParameter(reservation));
  }

  @ParameterizedTest
  @NullSource
  void whenDateIsNull_thenShouldThrow(LocalDate date) {
    when(reservation.getDate()).thenReturn(date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservation);
        });
  }

  @ParameterizedTest
  @NullSource
  void whenStartTimeIsNull_thenShouldThrow(LocalTime startTime) {
    when(reservation.getStartTime()).thenReturn(startTime);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservation);
        });
  }

  @ParameterizedTest
  @NullSource
  void whenCustomerIsNull_thenShouldThrow(Customer customer) {
    when(reservation.getCustomer()).thenReturn(customer);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservation);
        });
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void whenCustomerNameIsNullOrEmpty_thenShouldThrow(String name) {
    when(reservation.getCustomer().getName()).thenReturn(name);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservation);
        });
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void whenCustomerEmailIsNullOrEmpty_thenShouldThrow(String email) {
    when(reservation.getCustomer().getEmail()).thenReturn(email);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservation);
        });
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void whenCustomerPhoneNumberIsNullOrEmpty_thenShouldThrow(String phoneNumber) {
    when(reservation.getCustomer().getPhoneNumber()).thenReturn(phoneNumber);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservation);
        });
  }

  @Test
  void whenParametersValid_thenShouldNotThrow() {
    assertDoesNotThrow(
        () -> validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME));
  }

  @Test
  void whenReservationStartTimeAtClosingTime_thenShouldThrow() {
    when(reservation.getStartTime()).thenReturn(RESTAURANT_CLOSE_TIME);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }

  @Test
  void whenReservationStartTimeAfterClosingTime_thenShouldThrow() {
    when(reservation.getStartTime()).thenReturn(RESTAURANT_CLOSE_TIME.plusMinutes(10));

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void whenGroupSizeLesserThan1_thenShouldThrow(int groupSize) {
    when(reservation.getGroupSize()).thenReturn(groupSize);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"a111111111", "*-/1111111"})
  void whenCustomerPhoneNumberHasLetterOrSymbols_thenShouldThrow(String phoneNumber) {
    when(reservation.getCustomer().getPhoneNumber()).thenReturn(phoneNumber);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "12312345678"})
  void whenCustomerPhoneNumberDoesNotHave10Digits_thenShouldThrow(String phoneNumber) {
    when(reservation.getCustomer().getPhoneNumber()).thenReturn(phoneNumber);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"@test.com", "test1@.com", "test@test."})
  void whenCustomerEmailHasMissingParts_thenShouldThrow(String email) {
    when(reservation.getCustomer().getEmail()).thenReturn(email);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"test1test.com", "test1@testcom"})
  void whenCustomerEmailHasMissingSymbols_thenShouldThrow(String email) {
    when(reservation.getCustomer().getEmail()).thenReturn(email);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"test1*@test.com", "te@st1@test.com", "test1@test.c/om"})
  void whenCustomerEmailHasExtraSymbols_thenShouldThrow(String email) {
    when(reservation.getCustomer().getEmail()).thenReturn(email);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservation, RESTAURANT_CLOSE_TIME);
        });
  }
}
