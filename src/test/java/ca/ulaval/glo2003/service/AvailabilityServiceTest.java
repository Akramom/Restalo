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
  private Reservation reservation;
  private final String RESTAURANT_ID = "1";
  private final String RESERVATION_NUMBER = "res1";
  private final int GROUP_SIZE = 4;
  private LocalDate DATE = LocalDate.of(2002, 2, 20);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    availabilityService = new AvailabilityService(restaurantRepository);
    reservation =
        new Reservation(
            RESERVATION_NUMBER,
            DATE,
            LocalTime.of(12, 0),
            LocalTime.of(13, 0),
            GROUP_SIZE,
            new Customer());
  }

  @Test
  void
      givenDateAndRestaurantIdAndRestaurantNotExist_whenGetAvailabilities_thenShouldThrowNotFoundException()
          throws NotFoundException {

    when(restaurantRepository.isExistAvailabilityForADate(RESTAURANT_ID, DATE))
        .thenThrow(new NotFoundException(RESTAURANT_NOT_FOUND));

    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> availabilityService.getAvailabilities(RESTAURANT_ID, DATE));
    assertThat(exception.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void givenDateAndValidRestaurantId_whenGetAvailabilities_thenShouldReturnAvailabilities()
      throws NotFoundException {
    List<Availability> expectedAvailabilities = new ArrayList<>();
    when(restaurantRepository.getAvailabilitiesForADate(RESTAURANT_ID, DATE))
        .thenReturn(expectedAvailabilities);

    List<AvailabilityDto> actualAvailabilities =
        availabilityService.getAvailabilities(RESTAURANT_ID, DATE);

    assertThat(actualAvailabilities).isEqualTo(expectedAvailabilities);
  }

  @Test
  void whenReleaseAvailabilities_thenShouldReleaseAvailabilities() throws NotFoundException {

    List<Availability> availabilities = new ArrayList<>();
    when(restaurantRepository.getAvailabilitiesForADate(RESTAURANT_ID, DATE))
        .thenReturn(availabilities);

    availabilityService.releaseAvailibilities(reservation, RESTAURANT_ID);

    verify(restaurantRepository, times(availabilities.size())).updateAvailability(any());
  }

  @Test
  void whenReserveAvailabilities_thenShouldReserveAvailabilities()
      throws NotFoundException, InvalidParameterException {

    Availability availability = new Availability(LocalDateTime.of(DATE, LocalTime.of(12, 0)), 10);

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(availability);

    when(restaurantRepository.getAvailabilitiesForADate(RESTAURANT_ID, DATE))
        .thenReturn(availabilities);

    availabilityService.reserveAvailabilities(reservation, RESTAURANT_ID);

    verify(restaurantRepository, times(availabilities.size())).updateAvailability(any());
  }
}
