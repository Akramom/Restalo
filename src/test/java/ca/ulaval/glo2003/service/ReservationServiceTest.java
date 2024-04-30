package ca.ulaval.glo2003.service;

import static ca.ulaval.glo2003.util.Constante.RESERVATION_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.service.AvailabilityService;
import ca.ulaval.glo2003.application.service.ReservationService;
import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.RestaurantRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationServiceTest {

  private ReservationService reservationService;
  private RestaurantRepository restaurantRepository;
  private AvailabilityService availabilityService;
  private final ReservationAssembler reservationAssembler = new ReservationAssembler();
  public final String RESTAURANT_NAME = "un nom";
  private final String RESTAURANT_ID = "10000";
  private final String RESERVATION_ID = "20000";
  private final String OWNER_ID = "00001";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final LocalTime RESERVATION_START = LocalTime.of(10, 30, 45);
  private final LocalTime RESERVATION_END = LocalTime.of(11, 30, 45);
  private LocalDate DATE;
  private final int CAPACITY = 1;
  private final int DURATION = 70;
  private final int GROUP_SIZE = 2;
  private final String CUSTOMER_NAME = "John Doe";
  private final String EMAIL = "john@example.com";
  private final String PHONE_NUMBER = "1234567389";
  private ReservationDuration reservationDuration;
  private Hours hours;
  private Restaurant restaurant;
  private Reservation reservation;
  private ReservationDto reservationDto;
  private Customer customer;

  @BeforeEach
  void setUp() {
    restaurantRepository = mock(RestaurantRepository.class);
    availabilityService = mock(AvailabilityService.class);
    reservationService = new ReservationService(restaurantRepository);
    reservationService.setAvailabilityService(availabilityService);
    hours = new Hours(OPEN, CLOSE);
    reservationDuration = new ReservationDuration(DURATION);
    restaurant =
        new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    customer = new Customer(CUSTOMER_NAME, EMAIL, PHONE_NUMBER);
    DATE = LocalDate.now();
    reservation =
        new Reservation(
            RESERVATION_ID, DATE, RESERVATION_START, RESERVATION_END, GROUP_SIZE, customer);
    reservationDto = reservationAssembler.toDto(reservation);
  }

  @Test
  void
      givenReservationNumberAndReservationExists_WhenGetReservationByNumber_thenReturnsReservationDto()
          throws NotFoundException {
    when(restaurantRepository.getReservationByNumber(RESERVATION_ID)).thenReturn(reservation);
    when(restaurantRepository.getRestaurantByReservationNumber(RESERVATION_ID))
        .thenReturn(restaurant);

    ReservationDto reservationDto = reservationService.getReservationByNumber(RESERVATION_ID);

    assertNotNull(reservationDto);
    assertEquals(RESERVATION_ID, reservationDto.getNumber());
  }

  @Test
  void
      givenReservationNumberAndReservationDoesNotExist_whenGetReservationByNumber_thenThrowsNotFoundException()
          throws NotFoundException {
    String nonExistingReservationNumber = "nonExisting";

    when(restaurantRepository.getReservationByNumber(nonExistingReservationNumber))
        .thenThrow(new NotFoundException(RESERVATION_NOT_FOUND));

    assertThrows(
        NotFoundException.class,
        () -> reservationService.getReservationByNumber(nonExistingReservationNumber));
  }

  @Test
  void
      givenValidReservationNumberAndReservationExists_whenDeletesReservation_thenReservationIsDeleted()
          throws NotFoundException {
    when(restaurantRepository.getReservationByNumber(RESERVATION_ID)).thenReturn(reservation);
    when(restaurantRepository.getRestaurantByReservationNumber(RESERVATION_ID))
        .thenReturn(restaurant);

    reservationService.deleteReservation(RESERVATION_ID);

    verify(restaurantRepository, times(1)).deleteReservation(RESERVATION_ID, RESTAURANT_ID);
  }

  @Test
  void givenValidReservationSearchParameters_whenSearchReservation_ReturnsMatchingReservations()
      throws NotFoundException {
    List<Reservation> expectedReservations = new ArrayList<>();
    expectedReservations.add(reservation);
    when(restaurantRepository.getRerservationsByRestaurantId(OWNER_ID, RESTAURANT_ID))
        .thenReturn(expectedReservations);

    List<ReservationDto> result =
        reservationService.searchReservation(
            OWNER_ID, RESTAURANT_ID, DATE.toString(), CUSTOMER_NAME);
    Reservation actualReservation = reservationAssembler.fromDto(result.get(0));

    assertNotNull(result);
    assertEquals(expectedReservations.size(), result.size());
    assertEquals(expectedReservations.get(0), actualReservation);
  }
}
