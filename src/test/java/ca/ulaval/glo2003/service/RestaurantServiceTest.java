package ca.ulaval.glo2003.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.entity.*;
import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.exception.MissingParameterException;
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

  public final String UN_NOM = "un nom";
  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final int CAPACITY = 0;
  private Hours hours;
  private RestaurantService service;
  @Mock private Restaurant restaurant;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new RestaurantService();
    hours = new Hours(OPEN, CLOSE);
  }

  @Test
  void givenOwnerIdAndRestaurant_WhenAddRestaurant_thenRestaurantIsAddInRepository() {
    when(restaurant.getId()).thenReturn(RESTAURANT_ID);
    when(restaurant.getName()).thenReturn(UN_NOM);

    service.addRestaurant(OWNER_ID, restaurant);

    Restaurant unRestaurant = service.getRestaurantByIdOfOwner(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(restaurant);
    assertThat(unRestaurant.getName()).isEqualTo(UN_NOM);
    assertThat(unRestaurant.getId()).isEqualTo(RESTAURANT_ID);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotExistInRepository_thenGetRestaurantByIdOfOwnerReturnNull() {
    service.addOwner(OWNER_ID);

    Restaurant unRestaurant = service.getRestaurantByIdOfOwner(OWNER_ID, "autre id");

    assertThat(unRestaurant).isEqualTo(null);
  }

  @Test
  void givenOwneriD_WhenGetAllRestaurantsOfOwner_ThenReturnListOfRestaurantsOfOwner() {
    service.addRestaurant(OWNER_ID, restaurant);
    service.addRestaurant(OWNER_ID, restaurant);

    List<Restaurant> restaurantList = service.getAllRestaurantsOfOwner(OWNER_ID);

    assertThat(restaurantList).isNotEmpty();
    assertThat(restaurantList.size()).isEqualTo(2);
    assertThat(restaurantList).contains(restaurant);
  }

  @Test
  void givenOwnerId_whenOwnerExists_ThenAddOwnerReturnNull() {
    service.addOwner(OWNER_ID);

    Owner owner = service.addOwner(OWNER_ID);

    assertThat(owner).isNull();
  }

  @Test
  void givenOwnerId_whenExist_thenIsExistOwnerIdReturnTrue() {

    service.addOwner(OWNER_ID);

    assertEquals(true, service.isExistOwnerId(OWNER_ID));
  }

  @Test
  void givenOwnerId_whenNotExist_thenIsExistOwnerIdReturnFalse() {
    assertEquals(false, service.isExistOwnerId(OWNER_ID));
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  public void givenOwnerId_whenNullOrEmpty_TheVerifyOwnerIdThrowMissingParameterException(
      String ownerId) {

    MissingParameterException missingParameterException =
        assertThrows(MissingParameterException.class, () -> service.verifyOwnerId(ownerId));

    assertThat(missingParameterException.getMessage()).isEqualTo("Missing owner ID.");
  }

  @Test
  public void givenOwnerId_whenNotExist_TheVerifyOwnerIdThrowInvalidParameterException() {

    InvalidParameterException invalidParameterException =
        assertThrows(InvalidParameterException.class, () -> service.verifyOwnerId(OWNER_ID));

    assertThat(invalidParameterException.getMessage()).isEqualTo("Invalid owner ID.");
  }

  @Test
  public void
      givenRestaurant_whenNameIsNullorEmpty_thenVerifyRestaurantParameterThrowMissingParameterException() {

    when(restaurant.getName()).thenReturn(null);
    MissingParameterException missingParameterException =
        assertThrows(
            MissingParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(missingParameterException.getMessage()).isEqualTo("Missing restaurant parameter.");
  }

  @Test
  public void
      givenRestaurant_whenHoursIsNull_thenVerifyRestaurantParameterThrowMissingParameterException() {

    when(restaurant.getName()).thenReturn(UN_NOM);
    when(restaurant.getHours()).thenReturn(null);

    MissingParameterException missingParameterException =
        assertThrows(
            MissingParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(missingParameterException.getMessage()).isEqualTo("Missing restaurant parameter.");
  }

  @Test
  public void
      givenRestaurant_whenHoursIsInvald_thenVerifyRestaurantParameterThrowInvalidParameterException() {

    when(restaurant.getName()).thenReturn(UN_NOM);
    hours.setOpen(CLOSE);
    hours.setClose(OPEN);
    when(restaurant.getHours()).thenReturn(hours);

    InvalidParameterException invalidParameterException =
        assertThrows(
            InvalidParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(invalidParameterException.getMessage()).isEqualTo("Invalid restaurant parameter.");
  }

  @Test
  public void
      givenRestaurant_whenCapacityLessThanOne_thenVerifyRestaurantParameterThrowInvalidParameterException() {

    when(restaurant.getName()).thenReturn(UN_NOM);
    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getCapacity()).thenReturn(CAPACITY);

    InvalidParameterException invalidParameterException =
        assertThrows(
            InvalidParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(invalidParameterException.getMessage()).isEqualTo("Invalid restaurant parameter.");
  }

  @Test
  public void givenOwnerId_whenOwnerIdIsEmptyOrNull_verifyOwnerIdThrowMissingParameterException()
      throws Exception {

    service.addOwner(OWNER_ID);
    MissingParameterException missingParameterException =
        assertThrows(MissingParameterException.class, () -> service.verifyOwnerId(null));

    assertThat(missingParameterException.getMessage()).isEqualTo("Missing owner ID.");
  }

  @Test
  public void givenOwnerId_whenOwnerIdIsNostExist_verifyOwnerIdThrowMissingParameterException()
      throws Exception {

    InvalidParameterException invalidParameterException =
        assertThrows(InvalidParameterException.class, () -> service.verifyOwnerId(OWNER_ID));

    assertThat(invalidParameterException.getMessage()).isEqualTo("Invalid owner ID.");
  }
}
