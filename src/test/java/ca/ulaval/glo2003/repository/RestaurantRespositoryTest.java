package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.Reservation;
import ca.ulaval.glo2003.entity.ReservationDuration;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.exception.NotFoundException;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantRespositoryTest {

  public static final int CAPACITY = 100;
  private RestaurantRespository repository;
  private Restaurant restaurant;
  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";

  public static final String NOT_FOUND_MESSAGE = "No restaurant found for the owner.";

  public static final String UN_NOM = "un nom";
  private Hours hours;
  private ReservationDuration reservationDuration;

  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);

  @BeforeEach
  void setUp() {
    hours = new Hours(OPEN, CLOSE);
    reservationDuration = new ReservationDuration(70);
    restaurant = new Restaurant(RESTAURANT_ID, UN_NOM, CAPACITY, hours, reservationDuration);
    repository = new RestaurantRespository();
  }

  @Test
  void givenOwnerIdAndRestaurantId_WhenAddRestaurant_ThenRestaurantIsAddInRepository()
      throws NotFoundException {

    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant unRestaurant = repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(restaurant);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenGetRestaurantAndRestaurantIsInRepository_ThenReturnRestaurant()
          throws NotFoundException {

    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant aRestaurant = repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID);

    assertThat(aRestaurant).isEqualTo(restaurant);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotInRepository_ThenGetRestaurantShouldThrowNotFoundError()
          throws NotFoundException {
    repository.addOwner(OWNER_ID);

    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class,
            () -> repository.getOwnerRestaurantById(OWNER_ID, RESTAURANT_ID));

    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void givenAnOwnerId_whenGetAllRestaurants_thenReturnListOfRestaurantsForTheOwner() {

    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addRestaurant(OWNER_ID, restaurant);

    List<Restaurant> restaurantList = repository.getAllRestaurants(OWNER_ID);

    assertThat(restaurantList).isNotEmpty();
    assertThat(restaurantList.size()).isEqualTo(2);
    assertThat(restaurantList).contains(restaurant);
  }

  @Test
  void givenRestaurantIdInRepository_whenGetRestaurantById_thenReturnsRestaurant()
      throws NotFoundException {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant foundRestaurant = repository.getRestaurantById(RESTAURANT_ID);

    assertThat(foundRestaurant).isEqualTo(restaurant);
  }

  @Test
  void getReservationByNumber_WhenExists_ReturnsReservation() throws NotFoundException {
    Reservation expectedReservation = new Reservation();
    expectedReservation.setNumber("res123");
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);
    restaurant.addReservation(expectedReservation);

    Reservation actualReservation = repository.getReservationByNumber(expectedReservation.getNumber());

    assertThat(actualReservation).isNotNull();
    assertThat(actualReservation.getNumber()).isEqualTo(expectedReservation.getNumber());
  }

  @Test
  void getReservationByNumber_WhenNotExists_ThrowsNotFoundException() {

    String nonExistingReservationNumber = "nonExisting";
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    assertThrows(NotFoundException.class, () -> repository.getReservationByNumber(nonExistingReservationNumber));
  }
}
