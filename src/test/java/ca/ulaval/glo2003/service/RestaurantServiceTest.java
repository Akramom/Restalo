package ca.ulaval.glo2003.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.entity.*;
import ca.ulaval.glo2003.entity.Error;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantServiceTest {

  public static final String UN_NOM = "un nom";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final int CAPACITY = 0;
  private Hours hours;
  private RestaurantService service;
  @Mock private Restaurant restaurant;
  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new RestaurantService();
    hours = new Hours(OPEN, CLOSE);
  }

  @Test
  void canAddRestaurant() {
    when(restaurant.getId()).thenReturn(RESTAURANT_ID);
    when(restaurant.getName()).thenReturn("restau1");

    service.addRestaurantRepository(OWNER_ID, restaurant);

    Restaurant unRestaurant = service.getOwnerRestaurant(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(restaurant);
    assertThat(unRestaurant.getName()).isEqualTo("restau1");
    assertThat(unRestaurant.getId()).isEqualTo(RESTAURANT_ID);
  }

  @Test
  void getOwnerRestaurant_returnRestaurant_WhenRestaurantInRepository() {
    when(restaurant.getId()).thenReturn(RESTAURANT_ID);
    when(restaurant.getName()).thenReturn("restau1");
    service.addRestaurantRepository(OWNER_ID, restaurant);

    Restaurant unRestaurant = service.getOwnerRestaurant(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant.getName()).isEqualTo("restau1");
    assertThat(unRestaurant.getId()).isEqualTo(RESTAURANT_ID);
    assertThat(unRestaurant).isEqualTo(restaurant);
  }

  @Test
  void getOwnerRestaurant_returnNull_whenRestaurantNotInRepository() {
    service.addOwner(OWNER_ID);

    Restaurant unRestaurant = service.getOwnerRestaurant(OWNER_ID, "autre id");

    assertThat(unRestaurant).isEqualTo(null);
  }

  @Test
  void getAllOwnerRestaurants_returnListOfRestaurants() {
    service.addRestaurantRepository(OWNER_ID, restaurant);
    service.addRestaurantRepository(OWNER_ID, restaurant);

    List<Restaurant> restaurantList = service.getAllOwnerRestaurants(OWNER_ID);

    assertThat(restaurantList).isNotEmpty();
    assertThat(restaurantList.size()).isEqualTo(2);
    assertThat(restaurantList).contains(restaurant);
  }

  @Test
  void addOwner_returnNull_whenOwnerExists() {
    service.addOwner(OWNER_ID);

    Owner owner = service.addOwner(OWNER_ID);

    assertThat(owner).isNull();
  }

  @Test
  void isExistingNoOwner_returnTrue_whenOwnerExists() {
    service.addOwner(OWNER_ID);

    assertEquals(true, service.isExistingNoOwner(OWNER_ID));
  }

  @Test
  void isExistingNoOwner_returnFalse_whenOwnerExists() {
    assertEquals(false, service.isExistingNoOwner(OWNER_ID));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  public void givenOwner_whenOwnerIdIsNullOrEmpty_thenVerifyOwnerIdReturnMissingError(
      String ownerId) {

    Error missingError = service.verifyOwnerId(ownerId);

    assertThat(missingError).isNotNull();
    assertThat(missingError.getError()).isEqualTo(ErrorType.MISSING_PARAMETER);
    assertThat(missingError.getDescription()).isEqualTo("Missing owner ID.");
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  public void givenOwner_whenOwnerIdIsNotExist_thenVerifyOwnerIdReturnMissingError(String ownerId) {

    Error missingError = service.verifyCreateRestaurantReq(ownerId, restaurant);

    assertThat(missingError).isNotNull();
    assertThat(missingError.getError()).isEqualTo(ErrorType.MISSING_PARAMETER);
    assertThat(missingError.getDescription()).isEqualTo("Missing owner ID.");
  }

  @Test
  public void givenRestaurant_whenNameIsEmty_thenVerifyOwnerIdReturnInvalidError() {

    when(restaurant.getName()).thenReturn(null);
    Error invalidError = service.verifyCreateRestaurantReq(OWNER_ID, restaurant);

    assertThat(invalidError).isNotNull();
    assertThat(invalidError.getError()).isEqualTo(ErrorType.INVALID_PARAMETER);
    assertThat(invalidError.getDescription()).isEqualTo("Missing restaurant parameter.");
  }

  @Test
  public void givenRestaurant_whenHoursIsNull_thenVerifyOwnerIdReturnInvalidError() {

    when(restaurant.getName()).thenReturn(UN_NOM);
    when(restaurant.getHours()).thenReturn(null);
    Error invalidError = service.verifyCreateRestaurantReq(OWNER_ID, restaurant);

    assertThat(invalidError).isNotNull();
    assertThat(invalidError.getError()).isEqualTo(ErrorType.INVALID_PARAMETER);
    assertThat(invalidError.getDescription()).isEqualTo("Missing restaurant parameter.");
  }

  @Test
  public void givenRestaurant_whenHoursIsInvalid_thenVerifyOwnerIdReturnInvalidError() {

    when(restaurant.getName()).thenReturn(UN_NOM);
    hours.setOpen(CLOSE);
    hours.setClose(OPEN);
    when(restaurant.getHours()).thenReturn(hours);

    Error invalidError = service.verifyCreateRestaurantReq(OWNER_ID, restaurant);

    assertThat(invalidError).isNotNull();
    assertThat(invalidError.getError()).isEqualTo(ErrorType.INVALID_PARAMETER);
    assertThat(invalidError.getDescription()).isEqualTo("Invalid restaurant parameter.");
  }

  @Test
  public void givenRestaurant_whenCapasityisLesTanOne_thenVerifyOwnerIdReturnInvalidError() {

    when(restaurant.getName()).thenReturn(UN_NOM);
    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(CAPACITY);

    Error invalidError = service.verifyCreateRestaurantReq(OWNER_ID, restaurant);

    assertThat(invalidError).isNotNull();
    assertThat(invalidError.getError()).isEqualTo(ErrorType.INVALID_PARAMETER);
    assertThat(invalidError.getDescription()).isEqualTo("Invalid restaurant parameter.");
  }

  @Test
  public void givenRestaurant_whenRestaurantIsValid_thenVerifyOwnerIdReturnNull() {

    when(restaurant.getName()).thenReturn(UN_NOM);
    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(CAPACITY + 3);

    Error invalidError = service.verifyCreateRestaurantReq(OWNER_ID, restaurant);

    assertThat(invalidError).isNull();
  }

  @Test
  public void givenOwner_whenOwnerIdIsValidAndExist_thenVerifyOwnerIdNull() {

    service.addOwner(OWNER_ID);
    Error missingError = service.verifyOwnerId(OWNER_ID);

    assertThat(missingError).isNull();
  }

  @Test
  public void whenOwnerIdIsEmptyOrNull_verifyCreateRestaurantReqReturnMissingError() {

    service.addOwner(OWNER_ID);
    Error missingError = service.verifyOwnerId(OWNER_ID);

    assertThat(missingError).isNull();
  }
}
