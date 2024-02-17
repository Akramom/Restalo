package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.entity.*;
import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.util.ErrorBuilder;
import ca.ulaval.glo2003.util.Validator;
import java.util.List;

public class RestaurantService {

  private final RestaurantRespository restaurantRepository;
  private final Validator validator;
  private final ErrorBuilder errorBuilder;

  public RestaurantService() {
    this.restaurantRepository = new RestaurantRespository();
    this.validator = new Validator();
    this.errorBuilder = new ErrorBuilder();
  }

  public void addRestaurantRepository(String noOwner, Restaurant restaurant) {
    if (!isExistingNoOwner(noOwner)) {
      restaurantRepository.addOwner(noOwner);
    }
    restaurantRepository.addRestaurant(noOwner, restaurant);
  }

  public Restaurant getOwnerRestaurant(String noOwner, String noRestaurant) {
    return restaurantRepository.getRestaurant(noOwner, noRestaurant);
  }

  public List<Restaurant> getAllOwnerRestaurants(String noOwner) {
    return restaurantRepository.getAllRestaurants(noOwner);
  }

  public Owner addOwner(String ownerId) {
    if (isExistingNoOwner(ownerId)) return null;
    return restaurantRepository.addOwner(ownerId);
  }

  public Boolean isExistingNoOwner(String noOwner) {
    for (Owner owner : restaurantRepository.getOwner()) {
      String existingNoOwner = owner.getOwnerId();
      if (noOwner.equals(existingNoOwner)) {
        return true;
      }
    }
    return false;
  }

  public Error verifyOwnerId(String noOwner) {
    if (this.validator.checkStringEmpty(noOwner)) {
      return this.errorBuilder.missingError("Missing owner ID.");
    }
    if (!isExistingNoOwner(noOwner)) {
      return this.errorBuilder.invalidError("Invalid owner ID.");
    }
    return null;
  }

  public Error verifyCreateRestaurantReq(String noOwner, Restaurant restaurant) {
    if (this.validator.checkStringEmpty(noOwner)) {
      return this.errorBuilder.missingError("Missing owner ID.");
    }
    if (this.validator.emptyRestaurantParameter(restaurant)) {
      return this.errorBuilder.invalidError("Missing restaurant parameter.");
    }
    if (!this.validator.validRestaurant(restaurant)) {
      return this.errorBuilder.invalidError("Invalid restaurant parameter.");
    }
    return null;
  }
}
