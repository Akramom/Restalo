package ca.ulaval.glo2003.domain;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.domain.entity.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantTest {

  private final String NAME = "Poulet_Rouge";
  private final String OTHER_NAME = "Paris_Tacos";
  private final int RESERVATION_DURATION = 70;
  private final int CAPACITY = 100;
  private final int NEW_CAPACITY = 200;
  private final int GROUP_SIZE = 2;
  private final int DEFAULT_CAPACITY = 0;
  private final int ID_LENGTH = 8;
  private final String ID = "1001";
  private final String INVALID_ID = "invalid";
  private final LocalTime START_TIME = LocalTime.NOON;
  private final LocalTime END_TIME = LocalTime.MIDNIGHT;
  private LocalDateTime DATE;
  private final int NEW_REMAINING_PLACES = CAPACITY - GROUP_SIZE;
  private Restaurant restaurant;
  private Reservation reservation;
  private Hours hours;
  private ReservationDuration reservationDuration;

  @BeforeEach
  void setUp() {
    DATE = LocalDateTime.of(LocalDate.now(), START_TIME);
    hours = new Hours(START_TIME, END_TIME);
    reservationDuration = new ReservationDuration(RESERVATION_DURATION);
    restaurant = new Restaurant(ID, NAME, CAPACITY, hours, reservationDuration);
    reservation =
        new Reservation(ID, LocalDate.now(), START_TIME, END_TIME, GROUP_SIZE, new Customer());
  }

  @Test
  void whenGenerateId_thenNewRestaurantIdIsGenerated() {
    String initialId = restaurant.getId();

    restaurant.generateId();
    String newId = restaurant.getId();

    assertNotNull(newId);
    assertNotEquals(initialId, newId);
    assertEquals(ID_LENGTH, newId.length());
  }

  @Test
  void whenCreateRestaurantWithNoValues_thenDefaultValuesAreUsed() {
    Restaurant defaultRestaurant = new Restaurant();

    assertNull(defaultRestaurant.getName());
    assertEquals(DEFAULT_CAPACITY, defaultRestaurant.getCapacity());
    assertNull(defaultRestaurant.getHours());
    assertNull(defaultRestaurant.getReservation());
    assertNull(defaultRestaurant.getId());
  }

  @Test
  void whenSetNewName_thenNewNameIsSet() {
    restaurant.setName(OTHER_NAME);

    assertEquals(OTHER_NAME, restaurant.getName());
  }

  @Test
  void whenSetNewCapacity_thenNewCapacityIsSet() {
    restaurant.setCapacity(NEW_CAPACITY);

    assertEquals(NEW_CAPACITY, restaurant.getCapacity());
  }

  @Test
  void whenSetNewHours_thenNewHoursAreSet() {
    Hours newHours = new Hours(LocalTime.MIN, LocalTime.MAX);

    restaurant.setHours(newHours);

    assertEquals(newHours, restaurant.getHours());
  }

  @Test
  void whenSetNewReservationDuration_thenNewReservationDurationIsSet() {
    restaurant.setReservations(reservationDuration);

    assertEquals(reservationDuration, restaurant.getReservation());
  }

  @Test
  void givenExistingReservation_whenAddReservation_thenReservationIsAddedIntoReservationList() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);

    restaurant.addReservation(reservation);

    assertEquals(restaurant.getReservationList(), expectedList);
  }

  @Test
  void
      givenExistingReservation_whenRemoveReservation_thenReservationIsRemovedFromReservationList() {
    List<Reservation> expectedList = new ArrayList<>();
    restaurant.addReservation(reservation);

    restaurant.removeReservation(ID);

    assertEquals(restaurant.getReservationList(), expectedList);
  }

  @Test
  void
      givenNonExistentReservation_whenRemoveReservation_thenReservationIsRemovedFromReservationList() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);
    restaurant.addReservation(reservation);

    restaurant.removeReservation(INVALID_ID);

    assertEquals(restaurant.getReservationList(), expectedList);
  }

  @Test
  void whenAddAvailabilitiesToDate_thenRestaurantHasAvailabilitiesToDate() {
    List<Availability> availabilitiesBefore = new ArrayList<>(restaurant.getAvailabilities());
    LocalTime open = restaurant.getHours().getOpen();
    LocalTime lastReservationSlotPossible =
        restaurant.getHours().getClose().minusMinutes(restaurant.getReservation().duration());
    long expectedLength = Math.ceilDiv(MINUTES.between(open, lastReservationSlotPossible), 15);

    restaurant.addAvailabilities(LocalDate.now());

    assertNotEquals(availabilitiesBefore, restaurant.getAvailabilities());
    assertEquals(expectedLength, restaurant.getAvailabilities().size());
  }

  @Test
  void givenNewAvailability_whenSetAvailability_thenAvailabilityIsUpdated() {
    restaurant.addAvailabilities(LocalDate.now());
    String availabilityId = restaurant.getAvailabilities().get(0).getId();
    Availability newAvailability = new Availability(availabilityId, ID, DATE, NEW_REMAINING_PLACES);

    restaurant.setAvailability(newAvailability);

    assertEquals(NEW_REMAINING_PLACES, restaurant.getAvailabilities().get(0).getRemainingPlaces());
    assertEquals(CAPACITY, restaurant.getAvailabilities().get(1).getRemainingPlaces());
  }
}
