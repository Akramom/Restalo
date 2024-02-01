package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.entity.*;

import java.time.LocalTime;

public class RestaurantService {

    private final RestaurantRespository restaurantRepository;

    public RestaurantService() {
        this.restaurantRepository = new RestaurantRespository();
    }

    public Error verifyOwnerID(String _noOwner){
        if((_noOwner == null) || checkStringEmpty(_noOwner)){
            return createMissingError("Missing owner ID.");
        }
        if(!isExistingOwnerID(_noOwner)){
            return createInvalidError("Invalid owner ID.");
        }
        return null;
    }

    public Error verifyCreateRestaurantReq(Restaurant _restaurant){
        if(emptyRestaurantParameter(_restaurant)){
            return createMissingError("Missing restaurant parameter.");
        }
        if(!validRestaurant(_restaurant)){
            return createInvalidError("Invalid restaurant parameter.");
        }
        return null;
    }

    private Boolean isExistingOwnerID(String _noOwner){
        for (Owner owner: restaurantRepository.getOwner()){
            String noOwner = owner.getNoOwner();
            if (_noOwner.equals(noOwner) ){
                return true;
            }
        }
        return false;
    }

    private Boolean emptyRestaurantParameter(Restaurant _restaurant){
        String name = _restaurant.getName();
        if((name == null) || checkStringEmpty(name)){
            return true;
        }

        Hours hours = _restaurant.getHours();
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

    private Boolean validRestaurant(Restaurant _restaurant){
        if(!validOpeningHours(_restaurant.getHours())){
            return false;
        }
        if(!validCapacity(_restaurant.getCapacity())){
            return false;
        }
        return true;
    }


    private Boolean validCapacity(int _capacity){
        if(_capacity <= 0){
            return false;
        }
        return true;
    }

    private Boolean validOpeningHours(Hours _openingHours){
        LocalTime openTime = _openingHours.getOpen();
        LocalTime closeTime = _openingHours.getClose();

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


    public Restaurant addRestaurant(String noOwner, Restaurant newRestaurant) {
        return  restaurantRepository.addRestaurant(noOwner,newRestaurant);
    }

    private Error createInvalidError(String _description){
        return new Error(ErrorType.INVALID_PARAMETER, _description);
    }

    private Error createMissingError(String _description){
        return new Error(ErrorType.MISSING_PARAMETER, _description);
    }
}
