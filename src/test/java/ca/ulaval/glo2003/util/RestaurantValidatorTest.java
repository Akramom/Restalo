package ca.ulaval.glo2003.util;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.validator.RestaurantValidator;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import java.time.LocalTime;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantValidatorTest {

  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private RestaurantValidator validator;
  @Mock private RestaurantDto restaurantDto;
  private HoursDto hours;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    validator = new RestaurantValidator();
    hours = new HoursDto(OPEN, CLOSE);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 6, 8})
  void givenCapacityGreaterThanZero_validCapacityShouldReturnTrue(int capacity) {
    assertTrue(validator.isValidCapacity(capacity));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -2})
  void givenCapacityLesserThanOne_validCapacityShouldReturnFalse(int capacity) {
    assertFalse(validator.isValidCapacity(capacity));
  }

  @Test
  void givenHours_whenRestaurantOpensForAtLeast1Hour_thenValidOpeningHoursShouldReturnTrue() {
    assertTrue(validator.isValidOpeningHours(hours));
  }

  @Test
  void givenHours_whenRestaurantOpenLessThan1Hour_thenValidOpeningHoursShouldReturnFalse() {
    hours.setClose(OPEN);
    hours.setOpen(CLOSE);

    assertFalse(validator.isValidOpeningHours(hours));
  }

  @Test
  void givenARestaurant_whenHoursIsNotValid_thenValidRestaurantShouldReturnFalse() {
    hours.setClose(OPEN);
    hours.setOpen(CLOSE);

    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.capacity()).thenReturn(2);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validRestaurant(restaurantDto);
        });
  }

  @Test
  void givenARestaurant_whenCapacityIsNotValid_thenValidRestaurantShouldReturnFalse() {

    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.capacity()).thenReturn(0);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          validator.validRestaurant(restaurantDto);
        });
  }

  @Test
  void givenARestaurant_whenCapacityAndHoursAreValid_thenValidRestaurantShouldReturnTrue() {
    when(restaurantDto.hours()).thenReturn(hours);
    when(restaurantDto.capacity()).thenReturn(2);

    assertDoesNotThrow(() -> validator.validRestaurant(restaurantDto));
  }
}
