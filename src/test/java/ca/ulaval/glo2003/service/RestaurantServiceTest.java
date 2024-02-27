package ca.ulaval.glo2003.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

class RestaurantServiceTest {

  public final String UN_NOM = "un nom";
  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final int CAPACITY = 0;

  private final int reservationDuration = 70;
  private Hours hours;

  private RestaurantService service;
  private Restaurant restaurant;

  @BeforeEach
  void setUp() {
    service = new RestaurantService();
    hours = new Hours(OPEN, CLOSE);
    restaurant = new Restaurant(RESTAURANT_ID, UN_NOM, CAPACITY, hours, reservationDuration);
  }

  @Test
  void givenOwnerIdAndRestaurant_WhenAddRestaurant_thenRestaurantIsAddInRepository() {

    service.addRestaurant(OWNER_ID, restaurant);

    Restaurant unRestaurant = service.getRestaurantByOwnerAndRestaurantId(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(restaurant);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotExistInRepository_thenGetRestaurantByIdOfOwnerReturnNull() {
    service.addNewOwner(OWNER_ID);

    Restaurant unRestaurant = service.getRestaurantByOwnerAndRestaurantId(OWNER_ID, "autre id");

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
    service.addNewOwner(OWNER_ID);

    Owner owner = service.addNewOwner(OWNER_ID);

    assertThat(owner).isNull();
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

    restaurant.setName(null);
    MissingParameterException missingParameterException =
        assertThrows(
            MissingParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(missingParameterException.getMessage()).isEqualTo("Missing restaurant parameter.");
  }

  @Test
  public void
      givenRestaurant_whenHoursIsNull_thenVerifyRestaurantParameterThrowMissingParameterException() {

    restaurant.setHours(null);
    MissingParameterException missingParameterException =
        assertThrows(
            MissingParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(missingParameterException.getMessage()).isEqualTo("Missing restaurant parameter.");
  }

  @Test
  public void
      givenRestaurant_whenHoursIsInvald_thenVerifyRestaurantParameterThrowInvalidParameterException() {

    hours.setOpen(CLOSE);
    hours.setClose(OPEN);
    restaurant.setHours(hours);

    InvalidParameterException invalidParameterException =
        assertThrows(
            InvalidParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(invalidParameterException.getMessage()).isEqualTo("Invalid restaurant parameter.");
  }

  @Test
  public void
      givenRestaurant_whenCapacityLessThanOne_thenVerifyRestaurantParameterThrowInvalidParameterException() {

    InvalidParameterException invalidParameterException =
        assertThrows(
            InvalidParameterException.class, () -> service.verifyRestaurantParameter(restaurant));

    assertThat(invalidParameterException.getMessage()).isEqualTo("Invalid restaurant parameter.");
  }

  @Test
  public void givenOwnerId_whenOwnerIdIsEmptyOrNull_verifyOwnerIdThrowMissingParameterException()
      throws Exception {

    service.addNewOwner(OWNER_ID);
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
