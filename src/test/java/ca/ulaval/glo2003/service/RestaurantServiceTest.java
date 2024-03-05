package ca.ulaval.glo2003.service;

import static ca.ulaval.glo2003.util.Constante.*;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.api.response.restaurant.RestaurantPartialResponse;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantResponse;
import ca.ulaval.glo2003.application.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.domain.entity.Hours;
import ca.ulaval.glo2003.domain.entity.Owner;
import ca.ulaval.glo2003.domain.entity.ReservationDuration;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.repository.RestaurantRepository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class RestaurantServiceTest {
  public static final String NOT_FOUND_MESSAGE = "No restaurant found for the owner.";

  public final String UN_NOM = "un nom";
  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final int CAPACITY = 0;

  private ReservationDuration reservationDuration;
  private Hours hours;

  private RestaurantService service;
  private Restaurant restaurant;
  private RestaurantRepository restaurantRespository;
  private RestaurantAssembler restaurantAssembler;
  private RestaurantDto restaurantDto;

  @BeforeEach
  void setUp() {
    restaurantRespository = new RestaurantRepository();
    restaurantAssembler = new RestaurantAssembler();
    service = new RestaurantService(restaurantRespository);
    hours = new Hours(OPEN, CLOSE);
    reservationDuration = new ReservationDuration(70);
    restaurant = new Restaurant(RESTAURANT_ID, UN_NOM, CAPACITY, hours, reservationDuration);
    restaurantDto = restaurantAssembler.toDto(restaurant);
  }

  @Test
  void givenOwnerIdAndRestaurant_WhenAddRestaurant_thenRestaurantIsAddInRepository()
      throws NotFoundException {

    RestaurantPartialResponse restaurantResponse = service.addRestaurant(OWNER_ID, restaurantDto);

    assertAll(
        () -> {
          assertThat(restaurantResponse.getId()).isEqualTo(restaurant.getId());
          assertThat(restaurantResponse.getName()).isEqualTo(restaurant.getName());
          assertThat(restaurantResponse.getCapacity()).isEqualTo(restaurant.getCapacity());
          assertThat(restaurantResponse.getHours()).isEqualTo(restaurant.getHours());
          assertThat(restaurantResponse.getName()).isEqualTo(restaurant.getName());
        });
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotExistInRepository_thenGetRestaurantByIdOfOwnerShouldThrowNotFoundError()
          throws NotFoundException {
    service.addNewOwner(OWNER_ID);

    NotFoundException notFoundException =
        assertThrows(
            NotFoundException.class,
            () -> service.getRestaurantByIdOfOwner(OWNER_ID, "autre number"));

    assertThat(notFoundException.getMessage()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void givenOwneriD_WhenGetAllRestaurantsOfOwner_ThenReturnListOfRestaurantsOfOwner() {
    service.addRestaurant(OWNER_ID, restaurantDto);
    service.addRestaurant(OWNER_ID, restaurantDto);

    List<RestaurantResponse> restaurantList = service.getAllRestaurantsOfOwner(OWNER_ID);

    assertThat(restaurantList).isNotEmpty();
    assertThat(restaurantList.size()).isEqualTo(2);
    assertThat(restaurantList.get(0).getId()).isEqualTo(restaurant.getId());
    assertThat(restaurantList.get(0).getName()).isEqualTo(restaurant.getName());
  }

  @Test
  void givenOwnerId_whenOwnerAlreadyExists_ThenAddOwnerReturnNull() {
    service.addNewOwner(OWNER_ID);

    Owner owner = service.addNewOwner(OWNER_ID);

    assertThat(owner).isNull();
  }

  @Test
  void givenOwnerId_whenOwnerDoesNotExists_ThenAddOwnerReturnsNewOwnerInstance() {
    Owner expectedOwner = new Owner(OWNER_ID);

    Owner owner = service.addNewOwner(OWNER_ID);

    assertThat(owner.getOwnerId()).isEqualTo(expectedOwner.getOwnerId());
    assertThat(owner.getRestaurants()).isEqualTo(expectedOwner.getRestaurants());
  }

  @Test
  void givenOwnerId_whenExist_thenIsExistOwnerIdReturnTrue() {

    service.addNewOwner(OWNER_ID);

    assertEquals(true, service.isExistingOwnerId(OWNER_ID));
  }

  @Test
  void givenOwnerId_whenNotExist_thenIsExistOwnerIdReturnFalse() {
    assertEquals(false, service.isExistingOwnerId(OWNER_ID));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  public void givenOwnerId_whenNullOrEmpty_TheVerifyOwnerIdThrowMissingParameterException(
      String ownerId) {

    MissingParameterException missingParameterException =
        assertThrows(MissingParameterException.class, () -> service.verifyOwnerId(ownerId));

    assertThat(missingParameterException.getMessage()).isEqualTo(MISSING_OWNER_ID);
  }

  @Test
  public void
      givenRestaurant_whenNameIsNullorEmpty_thenVerifyRestaurantParameterThrowMissingParameterException() {

    restaurant.setName(null);
    restaurantDto = restaurantAssembler.toDto(restaurant);
    MissingParameterException missingParameterException =
        assertThrows(
            MissingParameterException.class,
            () -> service.verifyRestaurantParameter(restaurantDto));

    assertThat(missingParameterException.getMessage()).isEqualTo(MISSING_RESTAURANT_PARAMETER);
  }

  @Test
  public void
      givenRestaurant_whenHoursIsNull_thenVerifyRestaurantParameterThrowMissingParameterException() {

    restaurant.setHours(null);
    restaurantDto = restaurantAssembler.toDto(restaurant);
    MissingParameterException missingParameterException =
        assertThrows(
            MissingParameterException.class,
            () -> service.verifyRestaurantParameter(restaurantDto));

    assertThat(missingParameterException.getMessage()).isEqualTo(MISSING_RESTAURANT_PARAMETER);
  }

  @Test
  public void
      givenRestaurant_whenHoursIsInvald_thenVerifyRestaurantParameterThrowInvalidParameterException() {

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

    InvalidParameterException invalidParameterException =
        assertThrows(
            InvalidParameterException.class,
            () -> service.verifyRestaurantParameter(restaurantDto));

    assertThat(invalidParameterException.getMessage()).isEqualTo(INVALID_RESTAURANT_PARAMETER);
  }

  @Test
  public void givenOwnerId_whenOwnerIdIsEmptyOrNull_verifyOwnerIdThrowMissingParameterException()
      throws Exception {

    service.addNewOwner(OWNER_ID);
    MissingParameterException missingParameterException =
        assertThrows(MissingParameterException.class, () -> service.verifyOwnerId(null));

    assertThat(missingParameterException.getMessage()).isEqualTo(MISSING_OWNER_ID);
  }
}
