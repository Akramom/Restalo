package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.entity.Owner;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRespository {

  private List<Owner> owners;
  private List<Restaurant> restaurants;

  public RestaurantRespository() {
    owners = new ArrayList<>();
    restaurants = new ArrayList<>();
  }

  public List<Owner> getOwner() {
    return owners;
  }

  public Restaurant addRestaurant(String ownerId, Restaurant restaurant) {

    owners.stream()
        .filter(owner -> owner.getOwnerId().equals(ownerId))
        .toList()
        .get(0)
        .getRestaurants()
        .add(restaurant);
    restaurants.add(restaurant);

    return restaurant;
  }

  public Owner addOwner(String ownerId) {
    Owner owner = new Owner("Doe", "John", "418-222-2222");
    owner.setOwnerId(ownerId);
    owners.add(owner);
    return owner;
  }

  // Methode dans le repository pour retrouver un restaurant par son id
  public Restaurant getRestaurant(String ownerId, String restaurantId) throws NotFoundException {
    Restaurant unRestaurant =
        owners.stream()
            .filter(owner -> owner.getOwnerId().equals(ownerId))
            .toList()
            .get(0)
            .getRestaurants()
            .stream()
            .filter(restaurant -> restaurant.getId().equals(restaurantId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("No restaurant found for the owner."));
    return unRestaurant;
  }

  public List<Restaurant> getAllRestaurants(String ownerId) {
    List<Restaurant> ownerRestaurants =
        owners.stream()
            .filter(owner -> owner.getOwnerId().equals(ownerId))
            .toList()
            .get(0)
            .getRestaurants();
    return ownerRestaurants;
  }

  public Restaurant getRestaurantById(String restaurantId) {
    for (Restaurant restaurant : restaurants) {
      String id = restaurant.getId();
      if (id.equals(restaurantId)) {
        return restaurant;
      }
    }
    return null;
  }
}
