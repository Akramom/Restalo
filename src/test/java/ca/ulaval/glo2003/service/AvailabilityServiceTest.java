package ca.ulaval.glo2003.service;

import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.application.dtos.AvailabilityDto;
import ca.ulaval.glo2003.application.service.AvailabilityService;
import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.entity.Customer;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.RestaurantRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AvailabilityServiceTest {
  @Mock private RestaurantRepository restaurantRepository;

  private AvailabilityService availabilityService;

  String restaurantId;
  LocalDate date;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    availabilityService = new AvailabilityService(restaurantRepository);
    restaurantId = "1";
    date = LocalDate.now();
  }

  @Test
  void
      givenDateAndRestaurantIdAndRestaurantNotExist_WhenGetAvailabilities_shouldThrowNotFoundException()
          throws NotFoundException {

    when(restaurantRepository.isExistAvailabilityForADate(restaurantId, date))
        .thenThrow(new NotFoundException(RESTAURANT_NOT_FOUND));

    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> availabilityService.getAvailabilities(restaurantId, date));
    assertThat(exception.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void givenDateAndValidRestaurantId_WhenGetAvailabilities_theShouldReturnAvailabilities()
      throws NotFoundException {

    List<Availability> expectedAvailabilities = new ArrayList<>();
    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, date))
        .thenReturn(expectedAvailabilities);

    List<AvailabilityDto> actualAvailabilities =
        availabilityService.getAvailabilities(restaurantId, date);

    assertThat(actualAvailabilities).isEqualTo(expectedAvailabilities);
  }

  @Test
  void releaseAvailabilities_shouldReleaseAvailabilities() throws NotFoundException {

    Reservation reservation =
        new Reservation("res1", date, LocalTime.of(12, 0), LocalTime.of(13, 0), 4, new Customer());
    List<Availability> availabilities = new ArrayList<>();
    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, date))
        .thenReturn(availabilities);

    availabilityService.releaseAvailibilities(reservation, restaurantId);

    verify(restaurantRepository, times(availabilities.size())).updateAvailability(any());
  }

  @Test
  void reserveAvailabilities_shouldReserveAvailabilities()
      throws NotFoundException, InvalidParameterException {

    Reservation reservation =
        new Reservation("res1", date, LocalTime.of(12, 0), LocalTime.of(13, 0), 4, new Customer());

    Availability availability = new Availability(LocalDateTime.of(date, LocalTime.of(12, 0)), 10);

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(availability);

    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, date))
        .thenReturn(availabilities);

    availabilityService.reserveAvailabilities(reservation, restaurantId);

    verify(restaurantRepository, times(availabilities.size())).updateAvailability(any());
  }
}
