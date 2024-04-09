package ca.ulaval.glo2003.service;

import static ca.ulaval.glo2003.util.Constante.RESERVATION_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.dtos.CustomerDto;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.service.AvailabilityService;
import ca.ulaval.glo2003.application.service.ReservationService;
import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.IRestaurantRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationServiceTest {

  private ReservationService reservationService;
  private IRestaurantRepository restaurantRepository;
  private AvailabilityService availabilityService;
  private final ReservationAssembler reservationAssembler = new ReservationAssembler();

  @BeforeEach
  void setUp() {
    restaurantRepository = mock(IRestaurantRepository.class);
    availabilityService = mock(AvailabilityService.class);
    reservationService = new ReservationService(restaurantRepository);
    reservationService.setAvailabilityService(availabilityService);
  }

  @Test
  void
      givenReservationNumberAndReservationExists_WhenGetReservationByNumber_thenReturnsReservationDto()
          throws NotFoundException {

    String reservationNumber = "res123";
    Reservation reservation =
        new Reservation(
            reservationNumber,
            LocalDate.now(),
            LocalTime.of(12, 0),
            LocalTime.of(18, 0),
            2,
            new Customer("John Doe", "john@example.com", "123456789"));
    Restaurant restaurant = new Restaurant();
    when(restaurantRepository.getReservationByNumber(reservationNumber)).thenReturn(reservation);
    when(restaurantRepository.getRestaurantByReservationNumber(reservationNumber))
        .thenReturn(restaurant);

    ReservationDto reservationDto = reservationService.getReservationByNumber(reservationNumber);

    assertNotNull(reservationDto);
    assertEquals(reservationNumber, reservationDto.getNumber());
  }

  @Test
  void getReservationByNumber_whenNotExists_thenThrowsNotFoundException() throws NotFoundException {

    String nonExistingReservationNumber = "nonExisting";

    when(restaurantRepository.getReservationByNumber(nonExistingReservationNumber))
        .thenThrow(new NotFoundException(RESERVATION_NOT_FOUND));

    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> reservationService.getReservationByNumber(nonExistingReservationNumber));

    assertEquals(exception.getMessage(), RESERVATION_NOT_FOUND);
  }

  @Test
  void givenValidReservationParameter_WithValidReservationMatchingRestaurant_DoesNotThrowException()
      throws NotFoundException {

    String restaurantId = "rest123";
    ReservationDto reservationDto =
        new ReservationDto(
            "res123",
            LocalDate.now(),
            LocalTime.of(12, 0),
            LocalTime.of(18, 0),
            120,
            2,
            new CustomerDto("John Doe", "john@example.com", "1234567890"));
    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    restaurant.setHours(new Hours(LocalTime.of(10, 0), LocalTime.of(20, 0)));
    when(restaurantRepository.getRestaurantById(restaurantId)).thenReturn(restaurant);

    assertDoesNotThrow(
        () -> reservationService.verifyValidReservationParameter(restaurantId, reservationDto));
  }

  @Test
  void
      givenValidReservationParameter_withInvalidReservationToMatchingRestaurant_thenThrowsInvalidParameterException()
          throws NotFoundException {

    String restaurantId = "rest123";
    ReservationDto reservationDto =
        new ReservationDto(
            "res123",
            LocalDate.now(),
            LocalTime.of(9, 0),
            LocalTime.of(11, 0),
            120,
            2,
            new CustomerDto("John Doe", "john@example.com", "123456789"));
    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    restaurant.setHours(new Hours(LocalTime.of(10, 0), LocalTime.of(20, 0)));
    when(restaurantRepository.getRestaurantById(restaurantId)).thenReturn(restaurant);

    assertThrows(
        InvalidParameterException.class,
        () -> reservationService.verifyValidReservationParameter(restaurantId, reservationDto));
  }

  @Test
  void givenValidReservationNumberAndReservationExists_whenDeletesReservation_thenReservationIsDeleted()
      throws NotFoundException {

    String reservationNumber = "res123";
    String restaurantId = "rest123";
    Reservation reservation =
        new Reservation(
            reservationNumber,
            LocalDate.now(),
            LocalTime.of(12, 0),
            LocalTime.of(18, 0),
            2,
            new Customer("John Doe", "john@example.com", "123456789"));
    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    when(restaurantRepository.getReservationByNumber(reservationNumber)).thenReturn(reservation);
    when(restaurantRepository.getRestaurantByReservationNumber(reservationNumber))
        .thenReturn(restaurant);

    reservationService.deleteReservation(reservationNumber);

    verify(restaurantRepository, times(1)).deleteReservation(reservationNumber, restaurantId);
  }

  @Test
  void givenValidReservationSearchParameters_ReturnsMatchingReservations()
      throws NotFoundException {
    String ownerId = "owner123";
    String restaurantId = "rest123";
    LocalDate date = LocalDate.parse("2023-02-13");
    String customerName = "Alfred Lambert";
    List<Reservation> expectedReservations = createSampleReservations();
    when(restaurantRepository.getRerservationsByRestaurantId(ownerId, restaurantId))
        .thenReturn(expectedReservations);

    List<ReservationDto> result =
        reservationService.searchReservation(ownerId, restaurantId, date.toString(), customerName);

    assertNotNull(result);
    assertEquals(expectedReservations.size(), result.size());

    for (int i = 0; i < expectedReservations.size(); i++) {
      Reservation expectedReservation = expectedReservations.get(i);
      ReservationDto actualReservationDto = result.get(i);

      Reservation actualReservation = reservationAssembler.fromDto(actualReservationDto);

      assertEquals(expectedReservation, actualReservation);
    }
  }

  private List<Reservation> createSampleReservations() {
    List<Reservation> reservations = new ArrayList<>();
    reservations.add(
        new Reservation(
            "res123",
            LocalDate.parse("2023-02-13"),
            LocalTime.of(12, 0),
            LocalTime.of(14, 0),
            2,
            new Customer("Alfred Lambert", "alfred@example.com", "123456789")));
    return reservations;
  }
}
