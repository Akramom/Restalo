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

class ValidatorTest {

  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  @Mock private Restaurant restaurant;
  private Validator validator;
  private Hours hours;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    validator = new Validator();
    hours = new Hours(OPEN, CLOSE);
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 0, -3, 6})
  @Disabled
  void testValidCapacity(int capacity) {
    if (capacity > 0) assertTrue(validator.validCapacity(capacity));
    else assertFalse(validator.validCapacity(capacity));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 6, 8})
  void givenCapacityBiggerThanZezo_validCapacityShoulReturnTrue(int capacity) {
    assertTrue(validator.validCapacity(capacity));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -2})
  void givenCapacityLowerThanOne_validCapacityShoulReturnfalse(int capacity) {
    assertFalse(validator.validCapacity(capacity));
  }

  @Test
  void
      givenAnHours_WhenOpenIsLongerThanCloseOfAtLeastOneHour_ThenValidOpeningHoursShouldReturnTrue() {
    assertTrue(validator.validOpeningHours(hours));
  }

  @Test
  void
      givenAnHours_WhenOpenIslessThanCloseOfAtLeastOneHour_ThenValidOpeningHoursShouldReturnFalse() {

    hours.setClose(OPEN);
    hours.setOpen(CLOSE);
    assertFalse(validator.validOpeningHours(hours));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void givenAString_whenEmptyOrNull_ThencheckStringEmptyShouldReturnTrue(String value) {
    assertTrue(validator.checkStringEmpty(value));
  }

  @Test
  void givenARestaurant_WhenHoursIsNotValid_thenValidRestaurantShouldReturnFalse() {
    hours.setClose(OPEN);
    hours.setOpen(CLOSE);

    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(2);

    assertFalse(validator.validRestaurant(restaurant));
  }

  @Test
  void givenARestaurant_WhenCapacityIsNotValid_thenValidRestaurantShouldReturnFalse() {

    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(0);

    assertFalse(validator.validRestaurant(restaurant));
  }

  @Test
  void givenARestaurant_WhenCapacityAnHoursIsValid_thenValidRestaurantShouldReturnTrue() {

    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(2);

    assertTrue(validator.validRestaurant(restaurant));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  void givenARestaurant_WhenNameIsNullOrEmpty_thenEmptyRestaurantParameterShouldReturnTrue(
      String name) {

    when(restaurant.getName()).thenReturn(name);

    when(restaurant.getHours()).thenReturn(hours);

    assertTrue(validator.emptyRestaurantParameter(restaurant));
  }

  @ParameterizedTest
  @MethodSource("invalidHours")
  void givenARestaurant_WhenHoursIsNull_thenEmptyRestaurantParameterShouldReturnTrue(Hours hours) {

    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getName()).thenReturn("name");

    assertTrue(validator.emptyRestaurantParameter(restaurant));
  }

  @Test
  void
      givenARestaurant_WhenNameAndHoursIsNotNullOrEmpty_thenEmptyRestaurantParameterShouldReturnFalse() {

    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getName()).thenReturn("name");

    assertFalse(validator.emptyRestaurantParameter(restaurant));
  }

  private static Stream<Hours> invalidHours() {
    Hours hours1 = null;
    Hours hours2 = new Hours(null, LocalTime.now());
    Hours hours3 = new Hours(LocalTime.now(), null);

    return Stream.of(hours1, hours2, hours3);
  }
}
