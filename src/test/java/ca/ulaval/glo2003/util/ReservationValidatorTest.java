package ca.ulaval.glo2003.util;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.application.dtos.CustomerDto;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.validator.ReservationValidator;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
  @Mock private ReservationDto reservationDto;
  @Mock private CustomerDto customer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    when(reservationDto.getDate()).thenReturn(DATE);
    when(reservationDto.getStartTime()).thenReturn(START_TIME);
    when(reservationDto.getEndTime()).thenReturn(START_TIME.plusMinutes(RESERVATION_DURATION));
    when(reservationDto.getGroupSize()).thenReturn(GROUP_SIZE);
    when(reservationDto.getCustomer()).thenReturn(customer);
    when(reservationDto.getCustomer().name()).thenReturn(CUSTOMER_NAME);
    when(reservationDto.getCustomer().email()).thenReturn(EMAIL);
    when(reservationDto.getCustomer().phoneNumber()).thenReturn(PHONE_NUMBER);
    validator = new ReservationValidator();
  }

  @Test
  void whenReservationParametersNotNullOrEmpty_thenShouldNotThrow() {
    assertDoesNotThrow(() -> validator.isEmptyReservationParameter(reservationDto));
  }

  @ParameterizedTest
  @NullSource
  void whenDateIsNull_thenShouldThrow(LocalDate date) {
    when(reservationDto.getDate()).thenReturn(date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservationDto);
        });
  }

  @ParameterizedTest
  @NullSource
  void whenStartTimeIsNull_thenShouldThrow(LocalTime startTime) {
    when(reservationDto.getStartTime()).thenReturn(startTime);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservationDto);
        });
  }

  @ParameterizedTest
  @NullSource
  void whenCustomerIsNull_thenShouldThrow(CustomerDto customer) {
    when(reservationDto.getCustomer()).thenReturn(customer);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservationDto);
        });
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void whenCustomerNameIsNullOrEmpty_thenShouldThrow(String name) {
    when(reservationDto.getCustomer().name()).thenReturn(name);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservationDto);
        });
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void whenCustomerEmailIsNullOrEmpty_thenShouldThrow(String email) {
    when(reservationDto.getCustomer().email()).thenReturn(email);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservationDto);
        });
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void whenCustomerPhoneNumberIsNullOrEmpty_thenShouldThrow(String phoneNumber) {
    when(reservationDto.getCustomer().phoneNumber()).thenReturn(phoneNumber);

    assertThrows(
        MissingParameterException.class,
        () -> {
          validator.isEmptyReservationParameter(reservationDto);
        });
  }

  @Test
  void whenParametersValid_thenShouldNotThrow() {
    assertDoesNotThrow(
        () -> validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME));
  }

  @Test
  void whenReservationStartTimeAtClosingTime_thenShouldThrow() {
    when(reservationDto.getStartTime()).thenReturn(RESTAURANT_CLOSE_TIME);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }

  @Test
  void whenReservationStartTimeAfterClosingTime_thenShouldThrow() {
    when(reservationDto.getStartTime()).thenReturn(RESTAURANT_CLOSE_TIME.plusMinutes(10));

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void whenGroupSizeLesserThan1_thenShouldThrow(int groupSize) {
    when(reservationDto.getGroupSize()).thenReturn(groupSize);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"a111111111", "*-/1111111"})
  void whenCustomerPhoneNumberHasLetterOrSymbols_thenShouldThrow(String phoneNumber) {
    when(reservationDto.getCustomer().phoneNumber()).thenReturn(phoneNumber);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "12312345678"})
  void whenCustomerPhoneNumberDoesNotHave10Digits_thenShouldThrow(String phoneNumber) {
    when(reservationDto.getCustomer().phoneNumber()).thenReturn(phoneNumber);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"@test.com", "test1@.com", "test@test."})
  void whenCustomerEmailHasMissingParts_thenShouldThrow(String email) {
    when(reservationDto.getCustomer().email()).thenReturn(email);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"test1test.com", "test1@testcom"})
  void whenCustomerEmailHasMissingSymbols_thenShouldThrow(String email) {
    when(reservationDto.getCustomer().email()).thenReturn(email);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"test1*@test.com", "te@st1@test.com", "test1@test.c/om"})
  void whenCustomerEmailHasExtraSymbols_thenShouldThrow(String email) {
    when(reservationDto.getCustomer().email()).thenReturn(email);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validateReservationToRestaurant(reservationDto, RESTAURANT_CLOSE_TIME);
        });
  }
}
