package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantRepositoryInMemoryTest implements IRestaurantRepositoryTest {
  private RestaurantRepositoryInMemory repository;
  private Restaurant restaurant;

  private Owner owner;
  public static final String RESTAURANT_ID1 = "300";

  public static final int CAPACITY = 100;
  private Reservation reservation;
  private final String RESTAURANT_ID = "10000";

  private final String RESERVATION_ID = "20000";
  private final String OWNER_ID = "00001";
  public static final String RESTAURANT_NAME = "un nom";
  private final LocalTime RESTAURANT_OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime RESTAURANT_CLOSE = LocalTime.of(19, 30, 45);

  private Hours hours;
  private ReservationDuration reservationDuration;

  @Override
  @BeforeEach
  public void setUp() {
    hours = new Hours(RESTAURANT_OPEN, RESTAURANT_CLOSE);
    owner = new Owner("Equipe", "JOHN", "418-222-2222");
    owner.setOwnerId(OWNER_ID);
    reservationDuration = new ReservationDuration(70);
    restaurant =
        new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);

    repository = new RestaurantRepositoryInMemory();
    reservation =
        new Reservation(
            RESERVATION_ID,
            LocalDate.now(),
            LocalTime.of(12, 0),
            LocalTime.of(18, 0),
            2,
            new Customer());
  }

  @Override
  @Test
  public void givenOwnerIdAndRestaurantId_whenAddRestaurant_thenRestaurantIsAddInRepository()
      throws NotFoundException {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant unRestaurant = repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(restaurant);
  }

  @Override
  @Test
  public void
      givenOwnerIdAndRestaurantId_whenGetRestaurantAndRestaurantIsInRepository_thenReturnRestaurant()
          throws NotFoundException {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant aRestaurant = repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID);

    assertThat(aRestaurant).isEqualTo(restaurant);
  }

  @Override
  @Test
  public void
      givenOwnerIdAndRestaurantId_whenRestaurantNotInRepository_thenGetRestaurantShouldThrowNotFoundError()
          throws NotFoundException {
    repository.addOwner(OWNER_ID);

    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class,
            () -> repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID1));

    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Override
  @Test
  public void givenAnOwnerId_whenGetAllOwnerRestaurants_thenReturnListOfRestaurantsForTheOwner()
      throws NotFoundException {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addRestaurant(OWNER_ID, restaurant);

    List<Restaurant> restaurantList = repository.getAllOwnerRestaurants(OWNER_ID);

    assertThat(restaurantList).isNotEmpty();
    assertThat(restaurantList.size()).isEqualTo(2);
    assertThat(restaurantList).contains(restaurant);
  }

  @Override
  @Test
  public void givenRestaurantIdInRepository_whenGetRestaurantById_thenReturnsRestaurant()
      throws NotFoundException {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant foundRestaurant = repository.getRestaurantById(RESTAURANT_ID);

    assertThat(foundRestaurant).isEqualTo(restaurant);
  }

  @Override
  @Test
  public void
      givenRestaurantIdAndReservation_whenAddReservation_thenReservationIsAddedToRestaurant()
          throws NotFoundException {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Reservation addedReservation = repository.addReservation(reservation, RESTAURANT_ID);

    assertThat(addedReservation).isEqualTo(reservation);
    assertThat(repository.getReservationByNumber(RESERVATION_ID)).isEqualTo(reservation);
  }

  @Override
  @Test
  public void givenInvalidRestaurantId_whenAddReservation_thenThrowsNotFoundException() {
    String invalidRestaurantId = "99999";
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class,
            () -> repository.addReservation(reservation, invalidRestaurantId));
    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Override
  @Test
  public void getReservationByNumber_WhenExists_thenReturnsReservation() throws NotFoundException {

    reservation.setNumber("res123");
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addReservation(reservation, restaurant.getId());

    Reservation actualReservation = repository.getReservationByNumber(reservation.getNumber());

    assertThat(actualReservation).isNotNull();
    assertThat(actualReservation.getNumber()).isEqualTo(reservation.getNumber());
  }

  @Override
  @Test
  public void getReservationByNumber_whenNotExists_thenThrowsNotFoundException() {

    String nonExistingReservationNumber = "nonExisting";
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    assertThrows(
        NotFoundException.class,
        () -> repository.getReservationByNumber(nonExistingReservationNumber));
  }
}
