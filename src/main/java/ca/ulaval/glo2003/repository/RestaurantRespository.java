package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.entity.Owner;
import ca.ulaval.glo2003.entity.Restaurant;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRespository {

  private static List<Owner> owners;
  private List<Restaurant> restaurants;

  public RestaurantRespository() {
    owners = new ArrayList<>();
    restaurants = new ArrayList<>();
  }

  public List<Owner> getOwner() {
    return owners;
  }

  public Restaurant addRestaurant(String _noOwner, Restaurant _restaurant) {

    owners.stream()
        .filter(p -> p.getOwnerId().equals(_noOwner))
        .toList()
        .get(0)
        .getRestaurants()
        .add(_restaurant);
    restaurants.add(_restaurant);

    return _restaurant;
  }

  public void addOwner(String noOwner) {
    Owner owner = new Owner("Doe", "John", "418-222-2222");
    owner.setOwnerId(noOwner);
    owners.add(owner);
  }

  // Methode dans le repository pour retrouver un restaurant par son id
  public Restaurant getRestaurant(String _noOwner, String _noRestaurant) {
    Restaurant restaurant =
        owners.stream()
            .filter(p -> p.getOwnerId().equals(_noOwner))
            .toList()
            .get(0)
            .getRestaurants()
            .stream()
            .filter(r -> r.getId().equals(_noRestaurant))
            .findFirst()
            .orElse(null);
    return restaurant;
  }

  public List<Restaurant> getAllRestaurants(String _noOwner) {

    List<Restaurant> ownerRestaurants =
        owners.stream()
            .filter(p -> p.getOwnerId().equals(_noOwner))
            .toList()
            .get(0)
            .getRestaurants();
    return ownerRestaurants;
  }

  public Boolean noRestaurantExists(String noRestaurant) {
    for (Restaurant restaurant : restaurants) {
      if (restaurant.getId() == noRestaurant) {
        return true;
      }
    }
    return false;
  }
}
