package ca.ulaval.glo2003.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.Restaurant;
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
  private Hours hours;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    validator = new RestaurantValidator();
    hours = new Hours(OPEN, CLOSE);
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

    when(restaurant.getHours()).thenReturn(hours);

    assertFalse(validator.isValidRestaurant(restaurant));
  }

  @Test
  void givenARestaurant_WhenCapacityIsNotValid_thenValidRestaurantShouldReturnFalse() {
    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(0);

    assertFalse(validator.isValidRestaurant(restaurant));
  }

  @Test
  void givenARestaurant_WhenCapacityAndHoursAreValid_thenValidRestaurantShouldReturnTrue() {
    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(2);

    assertTrue(validator.isValidRestaurant(restaurant));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void givenARestaurant_WhenNameIsNullOrEmpty_thenEmptyRestaurantParameterShouldReturnTrue(
      String name) {

    when(restaurant.getName()).thenReturn(name);

    when(restaurant.getHours()).thenReturn(hours);

    assertTrue(validator.isRestaurantParameterEmpty(restaurant));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidHours")
  void givenARestaurant_WhenHoursIsNull_thenIsRestaurantParameterEmptyShouldReturnTrue(
      Hours hours) {

    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getName()).thenReturn("name");

    assertTrue(validator.isRestaurantParameterEmpty(restaurant));
  }

  @Test
  void
      givenARestaurant_WhenNameAndHoursIsNotNullOrEmpty_thenEmptyRestaurantParameterShouldReturnFalse() {

    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getName()).thenReturn("name");

    assertFalse(validator.isRestaurantParameterEmpty(restaurant));
  }

  private static Stream<Hours> provideInvalidHours() {
    Hours hours1 = null;
    Hours hours2 = new Hours(null, LocalTime.now());
    Hours hours3 = new Hours(LocalTime.now(), null);

    return Stream.of(hours1, hours2, hours3);
  }
}
