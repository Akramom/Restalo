package ca.ulaval.glo2003.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.search.Search;
import ca.ulaval.glo2003.domain.search.SearchRestaurantHelper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchRestaurantHelperTest {
  private SearchRestaurantHelper searchRestaurantHelper;
  private List<Restaurant> restaurants;
  private final String NAME = "Premier";
  private final String OTHER_NAME = "Deuxieme";
  private final String LAST_NAME = "Doe";
  private final String FIRST_NAME = "Jonh";
  private final String PHONE = "4184444444";
  private final String OTHER_LAST_NAME = "Tremblay";
  private final String OTHER_FIRST_NAME = "Alice";
  private final String OTHER_PHONE = "41855555555";
  private final int CAPACITY = 2;
  private final int OTHER_CAPACITY = 4;
  private final int DURATION = 70;
  private final int OTHER_DURATION = 120;
  private final String ID = "1001";
  private final String OTHER_ID = "2001";
  private final LocalTime START_TIME = LocalTime.of(3, 4, 5);
  private final LocalTime END_TIME = LocalTime.of(15, 15, 15);
  private final LocalTime OTHER_START_TIME = LocalTime.of(2, 2, 2);
  private final LocalTime OTHER_END_TIME = LocalTime.of(13, 3, 3);
  private final LocalTime AFTER_END_TIME = LocalTime.of(21, 21, 21);
  private LocalDate DATE = LocalDate.of(2002, 2, 20);
  private LocalDate OTHER_DATE = LocalDate.of(3003, 3, 30);
  private final Hours HOURS = new Hours(START_TIME, END_TIME);
  private final Hours OTHER_HOURS = new Hours(OTHER_START_TIME, OTHER_END_TIME);
  private final String NAME_INPUT = "E M";
  private Restaurant restaurant;
  private Restaurant otherRestaurant;
  private Opened opened;
  private Owner owner;
  private Owner otherOwner;
  private Search search;

  @BeforeEach
  void setUp() {
    searchRestaurantHelper = new SearchRestaurantHelper();
    owner = new Owner(LAST_NAME, FIRST_NAME, PHONE);
    otherOwner = new Owner(OTHER_LAST_NAME, OTHER_FIRST_NAME, OTHER_PHONE);
    restaurant = new Restaurant(ID, NAME, CAPACITY, HOURS, new ReservationDuration(DURATION));
    otherRestaurant =
        new Restaurant(
            OTHER_ID,
            OTHER_NAME,
            OTHER_CAPACITY,
            OTHER_HOURS,
            new ReservationDuration(OTHER_DURATION));
    restaurants = new ArrayList<>();
    restaurants.add(restaurant);
    restaurants.add(otherRestaurant);
  }

  @Test
  void
      givenOnlyOpeningHour_whenSearchByOpenedHour_thenReturnsRestaurantsOpenedBeforeOrAtThatTime() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(otherRestaurant);
    opened = new Opened(START_TIME, null);

    List<Restaurant> returnedList = searchRestaurantHelper.searchByOpenedHour(restaurants, opened);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenOnlyOpeningHourThatIsAfterClose_whenSearchByOpenedHour_thenReturnsEmptyList() {
    List<Restaurant> expectedList = new ArrayList<>();
    opened = new Opened(END_TIME, null);

    List<Restaurant> returnedList = searchRestaurantHelper.searchByOpenedHour(restaurants, opened);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void
      givenOnlyClosingHour_whenSearchByOpenedHour_thenReturnsRestaurantsClosingAfterOrAtThatTime() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(otherRestaurant);
    opened = new Opened(null, OTHER_END_TIME);

    List<Restaurant> returnedList = searchRestaurantHelper.searchByOpenedHour(restaurants, opened);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenOnlyClosingHourThatIsAfterClose_whenSearchByOpenedHour_thenReturnsEmptyList() {
    List<Restaurant> expectedList = new ArrayList<>();
    opened = new Opened(null, AFTER_END_TIME);

    List<Restaurant> returnedList = searchRestaurantHelper.searchByOpenedHour(restaurants, opened);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenOpeningAndClosing_whenSearchByOpenedHour_thenReturnsRestaurantsOpenedDuring() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(otherRestaurant);
    opened = new Opened(OTHER_START_TIME, OTHER_END_TIME);

    List<Restaurant> returnedList = searchRestaurantHelper.searchByOpenedHour(restaurants, opened);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void
      givenNameNotCaseOrSpaceSensitive_whenSearchByName_thenReturnsRestaurantsWithContainingName() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(otherRestaurant);

    List<Restaurant> returnedList = searchRestaurantHelper.searchByName(restaurants, NAME_INPUT);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenSearch_whenSearch_thenReturnsMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    opened = new Opened(START_TIME, END_TIME);
    search = new Search(NAME_INPUT, opened);

    List<Restaurant> returnedList = searchRestaurantHelper.search(restaurants, search);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenSearchWithoutFromTime_whenSearch_thenReturnsMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(otherRestaurant);
    opened = new Opened(null, OTHER_END_TIME);
    search = new Search(NAME_INPUT, opened);

    List<Restaurant> returnedList = searchRestaurantHelper.search(restaurants, search);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenSearchWithoutToTime_whenSearch_thenReturnsMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(otherRestaurant);
    opened = new Opened(OTHER_START_TIME, null);
    search = new Search(NAME_INPUT, opened);

    List<Restaurant> returnedList = searchRestaurantHelper.search(restaurants, search);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenSearch_whenSearchRestaurant_thenReturnsMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    opened = new Opened(START_TIME, END_TIME);
    search = new Search(NAME_INPUT, opened);

    List<Restaurant> returnedList = searchRestaurantHelper.searchRestaurant(restaurants, search);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenSearchWithoutName_whenSearchRestaurant_thenReturnsMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(otherRestaurant);
    opened = new Opened(OTHER_START_TIME, OTHER_END_TIME);
    search = new Search(null, opened);

    List<Restaurant> returnedList = searchRestaurantHelper.searchRestaurant(restaurants, search);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenSearchWithoutFromTime_whenSearchRestaurant_thenReturnsMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(otherRestaurant);
    opened = new Opened(null, OTHER_END_TIME);
    search = new Search(NAME_INPUT, opened);

    List<Restaurant> returnedList = searchRestaurantHelper.searchRestaurant(restaurants, search);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenSearchWithoutToTime_whenSearchRestaurant_thenReturnsMatchingRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(otherRestaurant);
    opened = new Opened(OTHER_START_TIME, null);
    search = new Search(NAME_INPUT, opened);

    List<Restaurant> returnedList = searchRestaurantHelper.searchRestaurant(restaurants, search);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenNoSearch_whenSearchRestaurant_thenReturnsAllRestaurants() {
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(otherRestaurant);

    List<Restaurant> returnedList = searchRestaurantHelper.searchRestaurant(restaurants, search);

    assertEquals(expectedList, returnedList);
  }
}
