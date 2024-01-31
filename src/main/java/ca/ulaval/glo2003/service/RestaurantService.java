package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.entity.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {

    private final RestaurantRespository restaurantRepository;

    public RestaurantService() {
        this.restaurantRepository = new RestaurantRespository();
    }

    public ca.ulaval.glo2003.entity.Error verifyOwnerID(int _ownerID){
        if(_ownerID == 0){
            return createMissingError("Missing owner ID.");
        }
        if(!isExistingOwnerID(_ownerID)){
            return createInvalidError("Invalid owner ID.");
        }
        return null;
    }

    public ca.ulaval.glo2003.entity.Error verifyCreateRestaurantReq(Restaurant _restaurant){
        if(emptyRestaurantParameter(_restaurant)){
            return createMissingError("Missing restaurant parameter.");
        }
        if(!validRestaurant(_restaurant)){
            return createInvalidError("Invalid restaurant parameter.");
        }
        return null;
    }

    private Boolean isExistingOwnerID(int _ID){
        for (Proprietaire owner: restaurantRepository.getProprietaires()){
            int ownerID = owner.getNoProprietaire();
            if (_ID == ownerID){
                return true;
            }
        }
        return false;
    }

    private Boolean emptyRestaurantParameter(Restaurant _restaurant){
        String name = _restaurant.getName();
        LocalTime openTime = _restaurant.getHours().getOpen();
        LocalTime closeTime = _restaurant.getHours().getClose();

        if(checkStringEmpty(name) || (name == null)){
            return true;
        }

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


    private Boolean checkStringEmpty(String _string){
        String stringWithoutSpaces = _string.replaceAll("\\s", "");
        if(stringWithoutSpaces.isEmpty()){
            return true;
        }
        return false;
    }

    private ca.ulaval.glo2003.entity.Error createInvalidError(String _description){
        return new ca.ulaval.glo2003.entity.Error(ErrorType.INVALID_PARAMETER, _description);
    }

    private ca.ulaval.glo2003.entity.Error createMissingError(String _description){
        return new ca.ulaval.glo2003.entity.Error(ErrorType.MISSING_PARAMETER, _description);
    }

}
