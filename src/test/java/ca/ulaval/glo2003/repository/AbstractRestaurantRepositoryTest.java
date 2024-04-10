package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
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
  public static final String OWNER_FIRST_NAME = "JOHN";
  public static final String OWNER_LAST_NAME = "Equipe";
  public static final String PHONE_NUMBER = "418-222-2222";
  public static final int RESERVATION_DURATION = 70;
  public static final int RESERVATION_GROUP_SIZE = 2;
  private static final LocalTime RESERVATION_START_TIME = LocalTime.of(12, 0);
  private static final LocalTime RESERVATION_CLOSE_TIME = LocalTime.of(18, 0);
  private static final LocalTime RESTAURANT_OPEN = LocalTime.of(10, 30, 45);
  private static final LocalTime RESTAURANT_CLOSE = LocalTime.of(19, 30, 45);

  private Hours hours;
  private ReservationDuration reservationDuration;

  @BeforeEach
  public void setUp() {
    repository = createPersistence();

    hours = new Hours(RESTAURANT_OPEN, RESTAURANT_CLOSE);
    owner = new Owner(OWNER_LAST_NAME, OWNER_FIRST_NAME, PHONE_NUMBER);
    owner.setOwnerId(OWNER_ID);
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
}
