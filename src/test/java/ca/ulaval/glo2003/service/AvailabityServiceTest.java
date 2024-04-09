package ca.ulaval.glo2003.service;

import static ca.ulaval.glo2003.util.Constante.AVAILABILITY_NOT_FOUND;
import static ca.ulaval.glo2003.util.Constante.MISSING_RESERVATION_DATE;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.application.dtos.AvailabilityDto;
import ca.ulaval.glo2003.application.service.AvailabilityService;
import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.entity.Customer;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.IRestaurantRepository;
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
  @Mock private IRestaurantRepository restaurantRepository;

  private AvailabilityService availabilityService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    availabilityService = new AvailabilityService(restaurantRepository);
  }

  @Test
  void getAvailabilities_shouldReturnAvailabilities() throws NotFoundException {
    String restaurantId = "1";
    LocalDate date = LocalDate.now();
    List<Availability> expectedAvailabilities = new ArrayList<>();
    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, date))
        .thenReturn(expectedAvailabilities);

    List<AvailabilityDto> actualAvailabilities =
        availabilityService.getAvailabilities(restaurantId, date);

    assertThat(actualAvailabilities).isEqualTo(expectedAvailabilities);
  }

  @Test
  void getAvailabilities_shouldThrowNotFoundException() throws NotFoundException {
    String restaurantId = "1";
    LocalDate date = LocalDate.now();
    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, date))
        .thenThrow(new NotFoundException(AVAILABILITY_NOT_FOUND));

    assertThrows(
        NotFoundException.class, () -> availabilityService.getAvailabilities(restaurantId, date));
  }

  @Test
  void releaseAvailabilities_shouldReleaseAvailabilities() throws NotFoundException {
    String restaurantId = "1";
    LocalDate date = LocalDate.now();
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
      throws NotFoundException, InvalidParameterException, MissingParameterException {
    String restaurantId = "1";
    LocalDate date = LocalDate.now();
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

  @Test
  void reserveAvailabilities_whenNoPlacesAvailable_thenShouldThrowInvalidParameterException()
      throws NotFoundException {
    String restaurantId = "1";
    LocalDate date = LocalDate.now();
    Reservation reservation =
        new Reservation(
            "res1", date, LocalTime.of(12, 0), LocalTime.of(13, 0), 100, new Customer());
    List<Availability> availabilities = new ArrayList<>();
    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, date))
        .thenReturn(availabilities);

    assertThrows(
        InvalidParameterException.class,
        () -> availabilityService.reserveAvailabilities(reservation, restaurantId));
  }

  @Test
  void reserveAvailabilities_shouldThrowMissingParameterException_WhenMissingReservationDate()
      throws NotFoundException {
    String restaurantId = "1";
    Reservation incompleteReservation =
        new Reservation("res1", null, LocalTime.of(12, 0), LocalTime.of(13, 0), 4, new Customer());

    List<Availability> availabilities = new ArrayList<>();
    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, LocalDate.now()))
        .thenReturn(availabilities);

    MissingParameterException thrown =
        assertThrows(
            MissingParameterException.class,
            () -> availabilityService.reserveAvailabilities(incompleteReservation, restaurantId));

    assertEquals(MISSING_RESERVATION_DATE, thrown.getMessage());
  }

  @Test
  void reserveAvailabilities_shouldThrowInvalidParameterException_WhenReservationIsInvalid()
      throws NotFoundException {
    String restaurantId = "1";
    LocalDate date = LocalDate.now();

    Reservation incompleteReservation =
        new Reservation("res1", null, LocalTime.of(12, 0), LocalTime.of(13, 0), 4, new Customer());

    LocalDate pastDate = LocalDate.now().minusDays(1);
    Reservation reservationWithPastDate =
        new Reservation(
            "res2", pastDate, LocalTime.of(12, 0), LocalTime.of(13, 0), 4, new Customer());

    List<Availability> availabilities = new ArrayList<>();
    Availability availability = new Availability(LocalDateTime.of(date, LocalTime.of(12, 0)), 10);
    availabilities.add(availability);

    when(restaurantRepository.getAvailabilitiesForADate(restaurantId, date))
        .thenReturn(availabilities);

    assertThrows(
        InvalidParameterException.class,
        () -> availabilityService.reserveAvailabilities(incompleteReservation, restaurantId));

    assertThrows(
        InvalidParameterException.class,
        () -> availabilityService.reserveAvailabilities(reservationWithPastDate, restaurantId));
  }
}
