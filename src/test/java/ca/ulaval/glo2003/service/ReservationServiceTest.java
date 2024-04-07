package ca.ulaval.glo2003.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

  private ReservationService service;
  private IRestaurantRepository repository;
  private AvailabilityService availabilityService;

  @BeforeEach
  void setUp() {
    repository = mock(IRestaurantRepository.class);
    availabilityService = mock(AvailabilityService.class);
    service = new ReservationService(repository);
    service.setAvailabilityService(availabilityService);
  }

  @Test
  void given_getReservationByNumber_WhenReservationExists_then_ReturnsReservationDto()
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
    when(repository.getReservationByNumber(reservationNumber)).thenReturn(reservation);
    when(repository.getRestaurantByReservationNumber(reservationNumber)).thenReturn(restaurant);

    ReservationDto reservationDto = service.getReservationByNumber(reservationNumber);

    assertNotNull(reservationDto);
    assertEquals(reservationNumber, reservationDto.getNumber());
  }

  @Test
  void verifyValidReservationParameter_WithValidParameters_DoesNotThrowException()
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
            new CustomerDto(
                "John Doe", "john@example.com", "1234567890")); // Corrected phone number
    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    restaurant.setHours(
        new Hours(LocalTime.of(10, 0), LocalTime.of(20, 0))); // Set restaurant hours
    when(repository.getRestaurantById(restaurantId)).thenReturn(restaurant);

    assertDoesNotThrow(() -> service.verifyValidReservationParameter(restaurantId, reservationDto));
  }

  @Test
  void verifyValidReservationParameter_WithInvalidParameters_ThrowsException()
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
    restaurant.setHours(
        new Hours(LocalTime.of(10, 0), LocalTime.of(20, 0))); // Set restaurant hours
    when(repository.getRestaurantById(restaurantId)).thenReturn(restaurant);

    assertThrows(
        InvalidParameterException.class,
        () -> service.verifyValidReservationParameter(restaurantId, reservationDto));
  }

  @Test
  void given_deleteReservation_WhenReservationExists_then_DeletesReservation()
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
    restaurant.setId(restaurantId); // Set restaurant ID
    when(repository.getReservationByNumber(reservationNumber)).thenReturn(reservation);
    when(repository.getRestaurantByReservationNumber(reservationNumber)).thenReturn(restaurant);

    service.deleteReservation(reservationNumber);

    verify(repository, times(1)).deleteReservation(reservationNumber, restaurantId);
  }

  @Test
  void given_searchReservation_ReturnsMatchingReservations() throws NotFoundException {

    String ownerId = "owner123";
    String restaurantId = "rest123";
    LocalDate date = LocalDate.parse("2023-02-13");
    String customerName = "Alfred Lambert";
    List<Reservation> reservations = createSampleReservations();
    when(repository.getRerservationsByRestaurantId(ownerId, restaurantId)).thenReturn(reservations);

    List<ReservationDto> result =
        service.searchReservation(ownerId, restaurantId, date.toString(), customerName);

    assertNotNull(result);
    assertEquals(reservations.size(), result.size());
    // assertIterableEquals(reservations, result);

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
