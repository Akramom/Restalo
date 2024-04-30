package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;
import static com.google.common.truth.Truth.assertThat;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

public abstract class AbstractRestaurantRepositoryTest {
  abstract IRestaurantRepository createPersistence();

  private IRestaurantRepository repository;

  private Restaurant restaurant;
  private Restaurant otherRestaurant;
  private Reservation reservation;
  private Reservation otherReservation;
  private Owner owner;
  public static final int CAPACITY = 100;
  public static final String NON_EXISTENT_ID = "500";
  private static final String RESTAURANT_ID = "10000";
  public static final String OTHER_RESTAURANT_ID = "300";
  private static final String RESERVATION_ID = "20000";
  private static final String OTHER_RESERVATION_ID = "20001";
  private static final String OWNER_ID = "00001";
  public static final String OTHER_OWNER_ID = "00002";
  public static final String RESTAURANT_NAME = "un nom";
  public static final int RESERVATION_DURATION = 70;
  public static final int RESERVATION_GROUP_SIZE = 2;
  private static final LocalTime RESERVATION_START_TIME = LocalTime.of(12, 0);
  private static final LocalTime RESERVATION_CLOSE_TIME = LocalTime.of(18, 0);
  private static final LocalTime RESTAURANT_OPEN = LocalTime.of(10, 30, 45);
  private static final LocalTime RESTAURANT_CLOSE = LocalTime.of(19, 30, 45);
  private final int NEW_REMAINING_PLACES = CAPACITY - RESERVATION_GROUP_SIZE;

  private Hours hours;
  private ReservationDuration reservationDuration;

  @BeforeEach
  public void setUp() {
    repository = createPersistence();

    hours = new Hours(RESTAURANT_OPEN, RESTAURANT_CLOSE);
    owner = new Owner(OWNER_ID);
    repository.addOwner(OWNER_ID);
    reservationDuration = new ReservationDuration(RESERVATION_DURATION);
    restaurant =
        new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    otherRestaurant =
        new Restaurant(OTHER_RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    reservation =
        new Reservation(
            RESERVATION_ID,
            LocalDate.now(),
            RESERVATION_START_TIME,
            RESERVATION_CLOSE_TIME,
            RESERVATION_GROUP_SIZE,
            new Customer());
    otherReservation =
        new Reservation(
            OTHER_RESERVATION_ID,
            LocalDate.now(),
            RESERVATION_START_TIME,
            RESERVATION_CLOSE_TIME,
            RESERVATION_GROUP_SIZE,
            new Customer());
  }

  @Test
  public void givenOwnerIdAndRestaurantId_whenAddRestaurant_thenRestaurantIsAddInRepository()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant unRestaurant = repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(restaurant);
  }

  @Test
  public void
      givenOwnerIdAndRestaurantId_whenGetRestaurantAndRestaurantIsInRepository_thenReturnRestaurant()
          throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant aRestaurant = repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID);

    assertThat(aRestaurant).isEqualTo(restaurant);
  }

  @Test
  public void
      givenOwnerIdAndRestaurantId_whenRestaurantNotInRepository_thenGetRestaurantShouldThrowNotFoundError() {
    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class,
            () -> repository.getOwnerRestaurantById(OWNER_ID, NON_EXISTENT_ID));

    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  public void givenAnOwnerId_whenGetAllOwnerRestaurants_thenReturnListOfRestaurantsForTheOwner()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addRestaurant(OWNER_ID, otherRestaurant);

    List<Restaurant> restaurantList = repository.getAllOwnerRestaurants(OWNER_ID);

    assertThat(restaurantList).isNotEmpty();
    assertThat(restaurantList.size()).isEqualTo(2);
    assertThat(restaurantList).contains(restaurant);
  }

  @Test
  public void givenRestaurantIdInRepository_whenGetRestaurantById_thenReturnsRestaurant()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant foundRestaurant = repository.getRestaurantById(RESTAURANT_ID);

    assertThat(foundRestaurant).isEqualTo(restaurant);
  }

  @Test
  public void
      givenRestaurantIdAndReservation_whenAddReservation_thenReservationIsAddedToRestaurant()
          throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);

    Reservation addedReservation = repository.addReservation(reservation, RESTAURANT_ID);

    assertThat(addedReservation).isEqualTo(reservation);
    assertThat(repository.getReservationByNumber(RESERVATION_ID)).isEqualTo(reservation);
  }

  @Test
  public void givenInvalidRestaurantId_whenAddReservation_thenThrowsNotFoundException()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);

    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class, () -> repository.addReservation(reservation, NON_EXISTENT_ID));
    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  public void givenReservationNumber_whenGetReservationByNumber_thenReturnsReservation()
      throws NotFoundException {
    reservation.setNumber(RESERVATION_ID);
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addReservation(reservation, restaurant.getId());

    Reservation actualReservation = repository.getReservationByNumber(reservation.getNumber());

    assertThat(actualReservation).isNotNull();
    assertThat(actualReservation.getNumber()).isEqualTo(reservation.getNumber());
  }

  @Test
  public void getReservationByNumber_whenNotExists_thenThrowsNotFoundException()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);

    assertThrows(NotFoundException.class, () -> repository.getReservationByNumber(NON_EXISTENT_ID));
  }

  @Test
  public void
      deleteOwnerRestaurantById_whenOwnerAndRestaurantIdValid_thenDeletesRestaurantAndReservations()
          throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addRestaurant(OWNER_ID, otherRestaurant);
    repository.addReservation(reservation, RESTAURANT_ID);
    repository.addReservation(otherReservation, RESTAURANT_ID);

    repository.deleteRestaurantIfOwner(RESTAURANT_ID, OWNER_ID);

    assertThrows(
        NotFoundException.class, () -> repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID));
    assertThrows(NotFoundException.class, () -> repository.getReservationByNumber(RESERVATION_ID));
    assertThrows(
        NotFoundException.class, () -> repository.getReservationByNumber(OTHER_RESERVATION_ID));
    assertDoesNotThrow(() -> repository.getOwnerRestaurantById(OWNER_ID, OTHER_RESTAURANT_ID));
  }

  @Test
  public void deleteOwnerRestaurantById_whenOwnerDoesntOwnRestaurant_thenThrowsNotFoundException()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addOwner(OTHER_OWNER_ID);

    assertThrows(
        NotFoundException.class,
        () -> repository.deleteRestaurantIfOwner(RESTAURANT_ID, OTHER_OWNER_ID));
  }

  @Test
  public void deleteOwnerRestaurantById_whenRestaurantDoesntExist_thenThrowsNotFoundException() {
    assertThrows(
        NotFoundException.class, () -> repository.deleteRestaurantIfOwner(RESTAURANT_ID, OWNER_ID));
  }

  @Test
  public void deleteReservation_whenOwnerAndRestaurantIdValid_thenDeletesReservation()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addReservation(reservation, RESTAURANT_ID);

    repository.deleteReservation(RESERVATION_ID, RESTAURANT_ID);

    assertThrows(NotFoundException.class, () -> repository.getReservationByNumber(RESERVATION_ID));
  }

  @Test
  void givenOwnerId_whenGetOwner_thenReturnsOwner() throws NotFoundException {
    Owner returnedOwner = repository.getOwner(OWNER_ID);

    assertEquals(returnedOwner.getOwnerId(), OWNER_ID);
  }

  @Test
  void givenNonOwnerId_whenGetOwner_thenThrowsNotFoundException() {
    assertThrows(NotFoundException.class, () -> repository.getOwner(NON_EXISTENT_ID));
  }

  @Test
  void whenGetAllOwners_thenReturnsAllOwners() {
    List<Owner> expectedList = new ArrayList<>();
    expectedList.add(owner);

    List<Owner> returnedList = repository.getOwners();

    assertEquals(expectedList.size(), returnedList.size());
    assertEquals(expectedList.get(0).getOwnerId(), returnedList.get(0).getOwnerId());
  }

  @Test
  void whenGetAllRestaurants_thenReturnsAllRestaurants() throws NotFoundException {
    List<Restaurant> expectedList = new ArrayList<>();
    repository.addRestaurant(OWNER_ID, restaurant);
    expectedList.add(restaurant);
    repository.addRestaurant(OWNER_ID, otherRestaurant);
    expectedList.add(otherRestaurant);

    List<Restaurant> returnedList = repository.getAllRestaurants();

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenReservationNumber_whenGetRestaurantByReservationNumber_thenReturnsRestaurant()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addReservation(reservation, RESTAURANT_ID);

    Restaurant returnedRestaurant = repository.getRestaurantByReservationNumber(RESERVATION_ID);

    assertEquals(restaurant, returnedRestaurant);
  }

  @Test
  void
      givenInvalidReservationNumber_whenGetRestaurantByReservationNumber_thenThrowsNotFoundException() {
    assertThrows(
        NotFoundException.class,
        () -> repository.getRestaurantByReservationNumber(NON_EXISTENT_ID));
  }

  @Test
  void givenDate_whenAddAvailabilitiesForADate_thenAvailabilitiesAreCreatedAtDate()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);
    List<Availability> availabilitiesBefore =
        new ArrayList<>(repository.getAvailabilitiesForADate(RESTAURANT_ID, LocalDate.now()));
    LocalTime lastReservationSlotPossible = RESTAURANT_CLOSE.minusMinutes(RESERVATION_DURATION);
    long expectedListLength =
        Math.ceilDiv(MINUTES.between(RESTAURANT_OPEN, lastReservationSlotPossible), 15);

    repository.addAvailabilitiesForADate(RESTAURANT_ID, LocalDate.now());
    List<Availability> returnedAvailabilities =
        repository.getAvailabilitiesForADate(RESTAURANT_ID, LocalDate.now());

    assertNotEquals(availabilitiesBefore, returnedAvailabilities);
    assertEquals(expectedListLength, returnedAvailabilities.size());
  }

  @Test
  void givenNewAvailability_whenUpdateAvailability_thenAvailibilityIsUpdated()
      throws NotFoundException {
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addAvailabilitiesForADate(RESTAURANT_ID, LocalDate.now());
    String availabilityId =
        repository.getAvailabilitiesForADate(RESTAURANT_ID, LocalDate.now()).get(0).getId();
    LocalDateTime date = LocalDateTime.of(LocalDate.now(), RESTAURANT_OPEN);
    Availability newAvailability = new Availability(date, NEW_REMAINING_PLACES);
    newAvailability.setRestaurantId(RESTAURANT_ID);
    newAvailability.setId(availabilityId);

    repository.updateAvailability(newAvailability);
    List<Availability> newAvailibilities =
        repository.getAvailabilitiesForADate(RESTAURANT_ID, LocalDate.now());

    assertEquals(newAvailibilities.get(0).getRemainingPlaces(), NEW_REMAINING_PLACES);
  }
}
