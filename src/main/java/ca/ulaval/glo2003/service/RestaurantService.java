package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.entity.*;
import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.exception.MissingParameterException;
import ca.ulaval.glo2003.exception.NotFoundException;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.util.Validator;
import java.util.List;

public class RestaurantService {

  private final RestaurantRespository restaurantRepository;
  private final Validator validator;

  public RestaurantService(RestaurantRespository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
    this.validator = new Validator();
  }

  public Restaurant addRestaurant(String noOwner, Restaurant restaurant) {
    if (!isExistOwnerId(noOwner)) {
      restaurantRepository.addOwner(noOwner);
    }
    restaurantRepository.addRestaurant(noOwner, restaurant);

    return restaurant;
  }

  public Restaurant getRestaurantByIdOfOwner(String ownerId, String restaurantId)
      throws NotFoundException {

    Restaurant restaurant = restaurantRepository.getRestaurant(ownerId, restaurantId);

    return restaurant;
  }

  public List<Restaurant> getAllRestaurantsOfOwner(String ownerId) {
    return restaurantRepository.getAllRestaurants(ownerId);
  }

  public Owner addOwner(String ownerId) {
    if (isExistOwnerId(ownerId)) return null;
    return restaurantRepository.addOwner(ownerId);
  }

  public Boolean isExistOwnerId(String Ownerid) {
    for (Owner owner : restaurantRepository.getOwner()) {
      String ownerId = owner.getOwnerId();
      if (Ownerid.equals(ownerId)) {
        return true;
      }
    }
    return false;
  }

  public boolean isValidOwnerId(String ownerId) throws Exception {
    if (this.validator.isStringEmpty(ownerId)) {
      throw new MissingParameterException("Missing owner ID.");
    }
    return true;
  }

  public void verifyOwnerId(String ownerId) throws Exception {

    isValidOwnerId(ownerId);

    if (!isExistOwnerId(ownerId)) {
      throw new InvalidParameterException("Invalid owner ID.");
    }
  }

  public boolean verifyRestaurantParameter(Restaurant restaurant) throws Exception {

    if (this.validator.isRestaurantParameterEmpty(restaurant))
      throw new MissingParameterException("Missing restaurant parameter.");

    if (!this.validator.isValidRestaurant(restaurant))
      throw new InvalidParameterException("Invalid restaurant parameter.");

    return true;
  }
}
