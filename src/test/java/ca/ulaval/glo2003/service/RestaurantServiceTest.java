package ca.ulaval.glo2003.service;

import static ca.ulaval.glo2003.util.Constante.*;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

import ca.ulaval.glo2003.application.assembler.ReservationAssembler;
import ca.ulaval.glo2003.application.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.application.assembler.SearchAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.service.AvailabilityService;
import ca.ulaval.glo2003.application.service.ReservationService;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.domain.search.Search;
import ca.ulaval.glo2003.repository.RestaurantRepositoryInMemory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantServiceTest {
  public final String RESTAURANT_NAME = "un nom";
  private final String RESTAURANT_ID = "10000";
  private final String SECOND_RESTAURANT_ID = "10001";
  private final String THIRD_RESTAURANT_ID = "10002";
  private final String RESERVATION_ID = "20000";
  private final String OWNER_ID = "00001";
  private final String INVALID_ID = "invalid";
  private final String TXT_INPUT = "l a v";
  private final String VALID_NAME_WITH_SPACES = "La vie";
  private final String VALID_NAME_WITH_CAPS = "LAVE LA";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final LocalTime SEARCH_FROM = LocalTime.of(13, 30, 45);
  private final LocalTime SEARCH_TO = LocalTime.of(15, 30, 45);
  private final Opened SEARCHED_FROM_TO_TIME = new Opened(SEARCH_FROM, SEARCH_TO);
  private final int CAPACITY = 10;
  private final int GROUP_SIZE = 2;
  private final String CUSTOMER_NAME = "john doe";
  private final String CUSTOMER_EMAIL = "jj@jj.jj";
  private final String CUSTOMER_PHONE = "4284444444";
  private final LocalTime START_TIME = LocalTime.of(12, 7);
  private final LocalTime ADJUSTED_START_TIME = LocalTime.of(12, 15);
  private Restaurant secondRestaurant;
  private Restaurant thirdRestaurant;
  private Search searchInput;
  private ReservationDuration reservationDuration;
  private Hours hours;
  private RestaurantService service;
  private Restaurant restaurant;
  private Reservation reservation;
  private ReservationAssembler reservationAssembler;
  private ReservationDto reservationDto;
  private RestaurantRepositoryInMemory restaurantRespository;
  private RestaurantAssembler restaurantAssembler;
  private RestaurantDto restaurantDto;
  private ReservationService reservationService;
  private AvailabilityService availabilityService;

  @BeforeEach
  void setUp() {
    restaurantRespository = spy(new RestaurantRepositoryInMemory());
    restaurantAssembler = new RestaurantAssembler();
    reservationAssembler = new ReservationAssembler();
    service = new RestaurantService(restaurantRespository);
    reservationService = new ReservationService(restaurantRespository);
    availabilityService = new AvailabilityService(restaurantRespository);
    service.setReservationService(reservationService);
    service.setAvailabilityService(availabilityService);
    hours = new Hours(OPEN, CLOSE);
    reservationDuration = new ReservationDuration(70);
    restaurant =
        new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    secondRestaurant =
        new Restaurant(SECOND_RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    thirdRestaurant =
        new Restaurant(THIRD_RESTAURANT_ID, RESTAURANT_NAME, CAPACITY, hours, reservationDuration);
    restaurantDto = restaurantAssembler.toDto(restaurant);
    reservation =
        new Reservation(
            RESERVATION_ID,
            LocalDate.now(),
            START_TIME,
            LocalTime.of(18, 0),
            GROUP_SIZE,
            new Customer(CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_PHONE));
    reservationDto = reservationAssembler.toDto(reservation);
    searchInput = new Search(RESTAURANT_NAME, SEARCHED_FROM_TO_TIME);
  }

  @Test
  void givenOwnerIdAndRestaurant_whenAddRestaurant_thenRestaurantIsAddInRepository()
      throws Exception {

    RestaurantDto addedRestaurant = service.addRestaurant(OWNER_ID, restaurantDto);

    assertAll(
        () -> {
          assertThat(addedRestaurant.id()).isEqualTo(restaurant.getId());
          assertThat(addedRestaurant.name()).isEqualTo(restaurant.getName());
          assertThat(addedRestaurant.capacity()).isEqualTo(restaurant.getCapacity());
          assertThat(addedRestaurant.hours().getOpen()).isEqualTo(restaurant.getHours().getOpen());
          assertThat(addedRestaurant.hours().getClose())
              .isEqualTo(restaurant.getHours().getClose());
          assertThat(addedRestaurant.name()).isEqualTo(restaurant.getName());
        });
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotExistInRepository_thenGetRestaurantByIdOfOwnerShouldThrowNotFoundError()
          throws NotFoundException {
    service.addOwnerIfNew(OWNER_ID);

    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class, () -> service.getRestaurantByIdOfOwner(OWNER_ID, INVALID_ID));

    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void givenOwnerId_whenGetAllRestaurantsOfOwner_thenReturnListOfRestaurantsOfOwner()
      throws Exception {
    service.addRestaurant(OWNER_ID, restaurantDto);
    service.addRestaurant(OWNER_ID, restaurantDto);

    List<RestaurantDto> restaurantList = service.getAllRestaurantsOfOwner(OWNER_ID);

    assertThat(restaurantList).isNotEmpty();
    assertThat(restaurantList.size()).isEqualTo(GROUP_SIZE);
    assertThat(restaurantList.get(0).id()).isEqualTo(restaurant.getId());
    assertThat(restaurantList.get(0).name()).isEqualTo(restaurant.getName());
  }

  @Test
  void givenOwnerId_whenOwnerAlreadyExists_thenAddOwnerReturnNull() {
    service.addOwnerIfNew(OWNER_ID);

    Owner owner = service.addOwnerIfNew(OWNER_ID);

    assertThat(owner).isNull();
  }

  @Test
  void givenOwnerId_whenOwnerDoesNotExists_thenAddOwnerReturnsNewOwnerInstance() {
    Owner expectedOwner = new Owner(OWNER_ID);

    Owner owner = service.addOwnerIfNew(OWNER_ID);

    assertThat(owner.getOwnerId()).isEqualTo(expectedOwner.getOwnerId());
    assertThat(owner.getRestaurants()).isEqualTo(expectedOwner.getRestaurants());
  }

  @Test
  void givenOwnerId_whenExist_thenIsExistOwnerIdReturnTrue() {

    service.addOwnerIfNew(OWNER_ID);

    assertEquals(true, service.isExistingOwnerId(OWNER_ID));
  }

  @Test
  void givenOwnerId_whenNotExist_thenIsExistOwnerIdReturnFalse() {
    assertEquals(false, service.isExistingOwnerId(OWNER_ID));
  }

  @Test
  public void
      givenRestaurant_whenHoursAreInvalid_thenVerifyRestaurantParameterThrowInvalidParameterException() {

    hours.setOpen(CLOSE);
    hours.setClose(OPEN);
    restaurant.setHours(hours);
    restaurantDto = restaurantAssembler.toDto(restaurant);

    InvalidParameterException invalidParameterException =
        assertThrows(
            InvalidParameterException.class,
            () -> service.verifyRestaurantParameter(restaurantDto));

    assertThat(invalidParameterException.getMessage()).isEqualTo(INVALID_RESTAURANT_PARAMETER);
  }

  @Test
  public void
      givenRestaurant_whenCapacityLessThanOne_thenVerifyRestaurantParameterThrowInvalidParameterException() {
    restaurant = new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, 0, hours, reservationDuration);

    restaurantDto = restaurantAssembler.toDto(restaurant);
    InvalidParameterException invalidParameterException =
        assertThrows(
            InvalidParameterException.class,
            () -> service.verifyRestaurantParameter(restaurantDto));

    assertThat(invalidParameterException.getMessage()).isEqualTo(INVALID_RESTAURANT_PARAMETER);
  }

  @Test
  void givenReservation_whenAddReservation_thenReservationIsReturned()
      throws InvalidParameterException, NotFoundException {
    restaurantRespository.addOwner(OWNER_ID);
    restaurantRespository.addRestaurant(OWNER_ID, restaurant);

    ReservationDto returnedReservation = service.addReservation(reservationDto, RESTAURANT_ID);

    assertEquals(returnedReservation.getNumber(), reservationDto.getNumber());
  }

  @Test
  void givenReservation_whenAddReservation_thenReturnedReservationStartTimeIsAdjusted()
      throws InvalidParameterException, NotFoundException {
    restaurantRespository.addOwner(OWNER_ID);
    restaurantRespository.addRestaurant(OWNER_ID, restaurant);

    ReservationDto returnedReservation = service.addReservation(reservationDto, RESTAURANT_ID);

    assertEquals(returnedReservation.getStartTime(), ADJUSTED_START_TIME);
  }

  @Test
  void givenReservation_whenAddReservation_thenReturnedReservationEndTimeIsAdjustedToRestaurant()
      throws InvalidParameterException, NotFoundException {
    restaurantRespository.addOwner(OWNER_ID);
    restaurantRespository.addRestaurant(OWNER_ID, restaurant);
    LocalTime expectedEndTime =
        ADJUSTED_START_TIME.plusMinutes(restaurant.getReservation().duration());

    ReservationDto returnedReservation = service.addReservation(reservationDto, RESTAURANT_ID);

    assertEquals(returnedReservation.getEndTime(), expectedEndTime);
  }

  @Test
  void getReservationByNumber_whenExists_thenReturnsReservation() throws NotFoundException {
    restaurantRespository.addOwner(OWNER_ID);
    restaurantRespository.addRestaurant(OWNER_ID, restaurant);
    restaurant.addReservation(reservation);

    Reservation actualReservation =
        restaurantRespository.getReservationByNumber(reservation.getNumber());

    assertThat(actualReservation).isNotNull();
    assertThat(actualReservation.getNumber()).isEqualTo(reservation.getNumber());
  }

  @Test
  void getReservationByNumber_whenNotExists_thenThrowsNotFoundException() throws NotFoundException {
    restaurantRespository.addOwner(OWNER_ID);
    restaurantRespository.addRestaurant(OWNER_ID, restaurant);

    assertThrows(
        NotFoundException.class, () -> restaurantRespository.getReservationByNumber(INVALID_ID));
  }

  @Test
  void givenNoParameterSearchInput_thenReturnsEveryRestaurants() throws NotFoundException {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedRestaurants = new ArrayList<>();
    expectedRestaurants.add(restaurant);
    expectedRestaurants.add(secondRestaurant);
    expectedRestaurants.add(thirdRestaurant);
    String name = null;
    Opened opened = null;
    searchInput.setName(name);
    searchInput.setOpened(opened);

    List<Restaurant> foundRestaurants =
        service.searchRestaurant(new SearchAssembler().toDto(searchInput)).stream()
            .map(new RestaurantAssembler()::fromDto)
            .collect(Collectors.toList());

    assertThat(foundRestaurants).isEqualTo(expectedRestaurants);
  }

  void addOwnerAndRestaurantsRepository() throws NotFoundException {
    restaurantRespository.addOwner(OWNER_ID);
    restaurantRespository.addRestaurant(OWNER_ID, restaurant);
    restaurantRespository.addRestaurant(OWNER_ID, secondRestaurant);
    restaurantRespository.addRestaurant(OWNER_ID, thirdRestaurant);
  }

  @Test
  void
      givenNameSearchInput_whenMatchingRestaurantNamesExistsNotCaseOrSpaceSensitive_thenReturnsListWithMatchingRestaurants()
          throws NotFoundException {
    List<Restaurant> expectedRestaurants = new ArrayList<>();
    restaurant.setName(VALID_NAME_WITH_SPACES);
    expectedRestaurants.add(restaurant);
    secondRestaurant.setName(VALID_NAME_WITH_CAPS);
    expectedRestaurants.add(secondRestaurant);
    addOwnerAndRestaurantsRepository();
    searchInput.setName(TXT_INPUT);

    List<Restaurant> foundRestaurants =
        service.searchRestaurant(new SearchAssembler().toDto(searchInput)).stream()
            .map(new RestaurantAssembler()::fromDto)
            .collect(Collectors.toList());

    assertThat(foundRestaurants).isEqualTo(expectedRestaurants);
  }

  @Test
  void givenOnlyNameSearchInput_thenReturnsListRestaurantsMatchingName() throws NotFoundException {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedRestaurants = new ArrayList<>();
    expectedRestaurants.add(restaurant);
    expectedRestaurants.add(secondRestaurant);
    expectedRestaurants.add(thirdRestaurant);
    Opened opened = null;
    searchInput.setOpened(opened);

    List<Restaurant> foundRestaurants =
        service.searchRestaurant(new SearchAssembler().toDto(searchInput)).stream()
            .map(new RestaurantAssembler()::fromDto)
            .collect(Collectors.toList());

    assertThat(foundRestaurants).isEqualTo(expectedRestaurants);
  }

  @Test
  void
      givenHoursSearchInput_whenMatchingRestaurantHoursExists_thenReturnListWithMatchingRestaurants()
          throws NotFoundException {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedRestaurants = new ArrayList<>();
    LocalTime openAfterFromSearchInput = LocalTime.of(13, 50, 45);
    Hours hours = new Hours(openAfterFromSearchInput, CLOSE);
    restaurant.setHours(hours);
    expectedRestaurants.add(secondRestaurant);
    expectedRestaurants.add(thirdRestaurant);

    List<Restaurant> matchingRestaurants =
        service.searchRestaurant(new SearchAssembler().toDto(searchInput)).stream()
            .map(new RestaurantAssembler()::fromDto)
            .collect(Collectors.toList());

    assertThat(matchingRestaurants).isEqualTo(expectedRestaurants);
  }

  @Test
  void givenOnlyOpenedSearchInput_thenReturnsListRestaurantsMatchingHours()
      throws NotFoundException {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedRestaurants = new ArrayList<>();
    expectedRestaurants.add(restaurant);
    expectedRestaurants.add(secondRestaurant);
    expectedRestaurants.add(thirdRestaurant);
    String name = null;
    searchInput.setName(name);

    List<Restaurant> foundRestaurants =
        service.searchRestaurant(new SearchAssembler().toDto(searchInput)).stream()
            .map(new RestaurantAssembler()::fromDto)
            .collect(Collectors.toList());

    assertThat(foundRestaurants).isEqualTo(expectedRestaurants);
  }

  @Test
  void givenOnlyFromHourSearchInput_thenReturnsListRestaurantsMatchingHours()
      throws NotFoundException {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);
    Opened openedToNull = new Opened(SEARCH_FROM, null);
    searchInput.setOpened(openedToNull);

    List<Restaurant> matchingRestaurants =
        service.searchRestaurant(new SearchAssembler().toDto(searchInput)).stream()
            .map(new RestaurantAssembler()::fromDto)
            .collect(Collectors.toList());

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }

  @Test
  void givenOnlyToHourSearchInput_thenReturnsListRestaurantsMatchingHours()
      throws NotFoundException {
    addOwnerAndRestaurantsRepository();
    List<Restaurant> expectedList = new ArrayList<>();
    expectedList.add(restaurant);
    expectedList.add(secondRestaurant);
    expectedList.add(thirdRestaurant);
    Opened openedFromNull = new Opened(null, SEARCH_TO);
    searchInput.setOpened(openedFromNull);

    List<Restaurant> matchingRestaurants =
        service.searchRestaurant(new SearchAssembler().toDto(searchInput)).stream()
            .map(restaurant -> this.restaurantAssembler.fromDto(restaurant))
            .collect(Collectors.toList());

    assertThat(matchingRestaurants).isEqualTo(expectedList);
  }
}
