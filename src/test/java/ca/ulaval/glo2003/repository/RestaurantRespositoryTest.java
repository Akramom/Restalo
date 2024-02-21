package ca.ulaval.glo2003.repository;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.entity.Restaurant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantRespositoryTest {

  private RestaurantRespository repository;
  @Mock private Restaurant restaurant;

  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";
  public static final String UN_NOM = "un nom";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    repository = new RestaurantRespository();
  }

  @Test
  void givenOwnerIdAndRestaurantId_WhenAddRestaurant_ThenRestaurantIsAddInRepository() {
    when(restaurant.getId()).thenReturn(RESTAURANT_ID);
    when(restaurant.getName()).thenReturn(UN_NOM);

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
    when(restaurant.getId()).thenReturn(RESTAURANT_ID);

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
