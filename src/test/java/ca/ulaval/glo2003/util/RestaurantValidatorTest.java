package ca.ulaval.glo2003.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.application.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.validator.RestaurantValidator;
import ca.ulaval.glo2003.domain.entity.Hours;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantValidatorTest {

  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  @Mock private Restaurant restaurant;
  private RestaurantValidator validator;
  private RestaurantAssembler restaurantAssembler;
  @Mock private RestaurantDto restaurantDto;
  private Hours hours;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    validator = new RestaurantValidator();
    hours = new Hours(OPEN, CLOSE);
    restaurantAssembler = new RestaurantAssembler();
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 6, 8})
  void givenCapacityGreaterThanZero_validCapacityShouldReturnTrue(int capacity) {
    assertTrue(validator.isValidCapacity(capacity));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -2})
  void givenCapacityLesserThanOne_validCapacityShouldReturnfalse(int capacity) {
    assertFalse(validator.isValidCapacity(capacity));
  }

  @Test
  void
      givenAnHours_WhenOpenIsLongerThanCloseOfAtLeastOneHour_ThenValidOpeningHoursShouldReturnTrue() {
    assertTrue(validator.isValidOpeningHours(hours));
  }

  @Test
  void
      givenAnHours_WhenOpenIsLessThanCloseOfAtLeastOneHour_ThenValidOpeningHoursShouldReturnFalse() {
    hours.setClose(OPEN);
    hours.setOpen(CLOSE);

    assertFalse(validator.isValidOpeningHours(hours));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void givenAString_whenEmptyOrNull_ThencheckStringEmptyShouldReturnTrue(String value) {
    assertTrue(validator.isStringEmpty(value));
  }

  @Test
  void givenARestaurant_WhenHoursIsNotValid_thenValidRestaurantShouldReturnFalse() {
    hours.setClose(OPEN);
    hours.setOpen(CLOSE);

    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.capacity()).thenReturn(2);

    assertFalse(validator.isValidRestaurant(restaurantDto));
  }

  @Test
  void givenARestaurant_WhenCapacityIsNotValid_thenValidRestaurantShouldReturnFalse() {

    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.capacity()).thenReturn(0);

    assertFalse(validator.isValidRestaurant(restaurantDto));
  }

  @Test
  void givenARestaurant_WhenCapacityAnHoursIsValid_thenValidRestaurantShouldReturnTrue() {

    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.capacity()).thenReturn(2);

    assertTrue(validator.isValidRestaurant(restaurantDto));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void givenARestaurant_WhenNameIsNullOrEmpty_thenEmptyRestaurantParameterShouldReturnTrue(
      String name) {

    when(restaurantDto.name()).thenReturn(name);

    when(restaurantDto.hours()).thenReturn(hours);

    assertTrue(validator.isRestaurantParameterEmpty(restaurantDto));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidHours")
  void givenARestaurant_WhenHoursIsNull_thenIsRestaurantParameterEmptyShouldReturnTrue(
      Hours hours) {

    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.name()).thenReturn("name");

    assertTrue(validator.isRestaurantParameterEmpty(restaurantDto));
  }

  @Test
  void
      givenARestaurant_WhenNameAndHoursIsNotNullOrEmpty_thenEmptyRestaurantParameterShouldReturnFalse() {

    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.name()).thenReturn("name");

    assertFalse(validator.isRestaurantParameterEmpty(restaurantDto));
  }

  private static Stream<Hours> provideInvalidHours() {
    Hours hours1 = null;
    Hours hours2 = new Hours(null, LocalTime.now());
    Hours hours3 = new Hours(LocalTime.now(), null);

    return Stream.of(hours1, hours2, hours3);
  }
}
