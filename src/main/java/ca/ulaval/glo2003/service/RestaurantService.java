package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.entity.*;

import java.time.LocalTime;
import java.util.List;

public class RestaurantService {

    private final RestaurantRespository restaurantRepository;

    public RestaurantService() {
        this.restaurantRepository = new RestaurantRespository();
    }
    public Restaurant getOwnerRestaurant(String noOwner, String noRestaurant){
        return restaurantRepository.getRestaurant(noOwner, noRestaurant);
    }

    public List<Restaurant> getAllOwnerRestaurants(String noOwner){
        return restaurantRepository.getAllRestaurants(noOwner);
    }

    public Error verifyCreateRestaurantReq(String noOwner, Restaurant restaurant){
        if(noOwner == null || checkStringEmpty(noOwner)){
            return createMissingError("Missing owner ID.");
        }
        if(emptyRestaurantParameter(restaurant)){
            return createMissingError("Missing restaurant parameter.");
        }
        if(!validRestaurant(restaurant)){
            return createInvalidError("Invalid restaurant parameter.");
        }
        return null;
    }

    public void addRestaurantRepository(String noOwner, Restaurant restaurant){
        if(!isExistingNoOwner(noOwner)){
            restaurantRepository.addOwner(noOwner);
        }
        restaurantRepository.addRestaurant(noOwner, restaurant);
    }

    public Error verifyNoOwner(String noOwner){
        if((noOwner == null) || checkStringEmpty(noOwner)){
            return createMissingError("Missing owner ID.");
        }
        if(!isExistingNoOwner(noOwner)){
            return createInvalidError("Invalid owner ID.");
        }
        return null;
    }

    public ca.ulaval.glo2003.entity.Error verifyGetRestaurantReq(String noOwner, String noRestaurant){
        Restaurant restaurant = restaurantRepository.getRestaurant(noOwner, noRestaurant);
        if(restaurant == null){
            return createInvalidError("Invalid restaurant ID.");
        }
        return null;
    }

    public Boolean noRestaurantExists(String noRestaurant){
        return restaurantRepository.restaurantExists(noRestaurant);
    }

    public Restaurant getOwnerRestaurant(String noOwner, String noRestaurant){
        return restaurantRepository.getRestaurant(noOwner, noRestaurant);
    }

    public List<Restaurant> getAllOwnerRestaurants(String noOwner){
        return restaurantRepository.getAllRestaurants(noOwner);
    }

    private Boolean isExistingNoOwner(String noOwner){
        for (Owner owner: restaurantRepository.getOwner()){
            String existingNoOwner = owner.getNoOwner();
            if (noOwner.equals(existingNoOwner) ){
                return true;
            }
        }
        return false;
    }

    private Boolean emptyRestaurantParameter(Restaurant restaurant){
        String name = restaurant.getName();
        if((name == null) || checkStringEmpty(name)){
            return true;
        }

        Hours hours = restaurant.getHours();
        if(hours == null){
            return true;
        }

        LocalTime openTime = hours.getOpen();
        LocalTime closeTime = hours.getClose();
        if(openTime == null || closeTime == null){
            return true;
        }

        return false;
    }

    private Boolean validRestaurant(Restaurant restaurant){
        if(!validOpeningHours(restaurant.getHours())){
            return false;
        }
        if(!validCapacity(restaurant.getCapacity())){
            return false;
        }
        return true;
    }

    private Boolean validCapacity(int capacity){
        if(capacity <= 0){
            return false;
        }
        return true;
    }

    private Boolean validOpeningHours(Hours openingHours){
        LocalTime openTime = openingHours.getOpen();
        LocalTime closeTime = openingHours.getClose();

        int timeOpen = closeTime.getHour() - openTime.getHour();
        if (timeOpen < 1){
            return false;
        }

        return true;
    }


    private Boolean checkStringEmpty(String value){
        String stringWithoutSpaces = value.replaceAll("\\s", "");
        if(stringWithoutSpaces.isEmpty()){
            return true;
        }
        return false;
    }

    private Error createInvalidError(String description){
        return new Error(ErrorType.INVALID_PARAMETER, description);
    }

    private Error createMissingError(String description){
        return new Error(ErrorType.MISSING_PARAMETER, description);
    }
}
