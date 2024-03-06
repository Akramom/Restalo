package ca.ulaval.glo2003.repository;

import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantRespositoryTest {
  private RestaurantRepository repository;
  private Restaurant restaurant;
  private Restaurant secondRestaurant;
  private Restaurant thirdRestaurant;
  private Owner owner;
  private Search searchInput;
  public static final int CAPACITY = 100;
  private Reservation reservation;
  private final String RESTAURANT_ID = "10000";
  private final String SECOND_RESTAURANT_ID = "10001";
  private final String THIRD_RESTAURANT_ID = "10002";
  private final String RESERVATION_ID = "20000";
  private final String OWNER_ID = "00001";
  public static final String RESTAURANT_NAME = "un nom";
  private final LocalTime RESTAURANT_OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime RESTAURANT_CLOSE = LocalTime.of(19, 30, 45);
  private final LocalTime SEARCH_FROM = LocalTime.of(13, 30, 45);
  private final LocalTime SEARCH_TO = LocalTime.of(15, 30, 45);
  private final Opened SEARCHED_FROM_TO_TIME = new Opened(SEARCH_FROM, SEARCH_TO);
  private Hours hours;
  private ReservationDuration reservationDuration;

  @BeforeEach
  void setUp() {
    hours = new Hours(RESTAURANT_OPEN, RESTAURANT_CLOSE);
    owner = new Owner("Equipe", "JOHN", "418-222-2222");
    owner.setOwnerId(OWNER_ID);
    reservationDuration = new ReservationDuration(70);
    restaurant =
        new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    secondRestaurant =
        new Restaurant(SECOND_RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    thirdRestaurant =
        new Restaurant(THIRD_RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    repository = new RestaurantRepository();
    reservation =
        new Reservation(
            RESERVATION_ID,
            LocalDate.now(),
            LocalTime.of(12, 0),
            LocalTime.of(18, 0),
            2,
            new Customer());
    searchInput = new Search(RESTAURANT_NAME, SEARCHED_FROM_TO_TIME);
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
  void givenRestaurantIdAndReservation_whenAddReservation_thenReservationIsAddedToRestaurant()
      throws NotFoundException {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Reservation addedReservation = repository.addReservation(reservation, RESTAURANT_ID);

    assertThat(addedReservation).isEqualTo(reservation);
    assertThat(repository.getReservationByNumber(RESERVATION_ID)).isEqualTo(reservation);
  }

  @Test
  void givenInvalidRestaurantId_whenAddReservation_thenThrowsNotFoundException() {
    String invalidRestaurantId = "99999";
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class,
            () -> repository.addReservation(reservation, invalidRestaurantId));
    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void givenNoParameterSearchInput_thenReturnsEveryRestaurants() {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);
    String name = null;
    Opened opened = null;
    searchInput.setName(name);
    searchInput.setOpened(opened);

    List<Restaurant> matchingRestaurants = repository.search(searchInput);

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }

  void addOwnerAndRestaurantsRepository() {
    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);
    repository.addRestaurant(OWNER_ID, secondRestaurant);
    repository.addRestaurant(OWNER_ID, thirdRestaurant);
  }

  @Test
  void
      givenNameSearchInput_whenMatchingRestaurantNamesExistsNotCaseOrSpaceSensitive_thenReturnsListWithMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    restaurant.setName("La vie");
    expectedList.add(restaurant);
    secondRestaurant.setName("Lave La");
    expectedList.add(secondRestaurant);
    addOwnerAndRestaurantsRepository();
    searchInput.setName("l a v");

    List<Restaurant> matchingRestaurants = repository.search(searchInput);

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }

  @Test
  void givenOnlyNameSearchInput_thenReturnsListRestaurantsMatchingName() {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);
    Opened opened = null;
    searchInput.setOpened(opened);

    List<Restaurant> matchingRestaurants = repository.search(searchInput);

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }

  @Test
  void
      givenHoursSearchInput_whenMatchingRestaurantHoursExists_thenReturnListWithMatchingRestaurants() {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    LocalTime openAfterFromSearchInput = LocalTime.of(13, 50, 45);
    Hours hours = new Hours(openAfterFromSearchInput, RESTAURANT_CLOSE);
    restaurant.setHours(hours);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);

    List<Restaurant> matchingRestaurants = repository.search(searchInput);

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }

  @Test
  void givenOnlyOpenedSearchInput_thenReturnsListRestaurantsMatchingHours() {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);
    String name = null;
    searchInput.setName(name);

    List<Restaurant> matchingRestaurants = repository.search(searchInput);

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }

  @Test
  void givenOnlyFromHourSearchInput_thenReturnsListRestaurantsMatchingHours() {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);
    Opened openedToNull = new Opened(SEARCH_FROM, null);
    searchInput.setOpened(openedToNull);

    List<Restaurant> matchingRestaurants = repository.search(searchInput);

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }

  @Test
  void givenOnlyToHourSearchInput_thenReturnsListRestaurantsMatchingHours() {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);
    Opened openedFromNull = new Opened(null, SEARCH_TO);
    searchInput.setOpened(openedFromNull);

    List<Restaurant> matchingRestaurants = repository.search(searchInput);

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }
}
