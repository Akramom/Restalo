package ca.ulaval.glo2003.repository;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.Restaurant;
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
  public static final String UN_NOM = "un nom";
  private Hours hours;
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);

  @BeforeEach
  void setUp() {
    hours = new Hours(OPEN, CLOSE);
    restaurant = new Restaurant(RESTAURANT_ID, UN_NOM, CAPACITY, hours);
    repository = new RestaurantRespository();
  }

  @Test
  void givenOwnerIdAndRestaurantId_WhenAddRestaurant_ThenRestaurantIsAddInRepository() {

    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    Restaurant unRestaurant = repository.getRestaurant(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(restaurant);
    assertThat(unRestaurant.getName()).isEqualTo(UN_NOM);
    assertThat(unRestaurant.getId()).isEqualTo(RESTAURANT_ID);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenGetRestaurantAndRestaurantIsInRepository_ThenReturnRestaurant() {

    repository.addOwner(OWNER_ID);
    repository.addRestaurant(OWNER_ID, restaurant);

    assertThat(repository.getOwner().size()).isEqualTo(1);
    assertThat(repository.getOwner().get(0).getOwnerId()).isEqualTo(OWNER_ID);
    assertThat(repository.getOwner().get(0).getRestaurants().get(0).getId())
        .isEqualTo(RESTAURANT_ID);
  }

  @Test
  void givenOwnerIdAndRestaurantId_whenGetRestaurantAndRestaurantNotInRepository_ThenReturnNull() {
    repository.addOwner(OWNER_ID);

    Restaurant unRestaurant = repository.getRestaurant(OWNER_ID, RESTAURANT_ID);

    assertThat(unRestaurant).isEqualTo(null);
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
}
