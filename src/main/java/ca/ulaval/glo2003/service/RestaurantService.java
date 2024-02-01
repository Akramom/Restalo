package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.entity.*;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.repository.*;

import java.time.LocalTime;
import java.util.List;

public class RestaurantService {

    public RestaurantRespository restaurantRepository;

    public RestaurantService() {
        this.restaurantRepository = new RestaurantRespository();
    }
    private ca.ulaval.glo2003.entity.Error createInvalidError(String _description){
        return new ca.ulaval.glo2003.entity.Error(ErrorType.INVALID_PARAMETER, _description);
    }
//    private Boolean isExistingOwnerID(int _ID){
//        for (Proprietaire owner: restaurantRepository.getProprietaires()){
//            int ownerID = owner.getNoProprietaire();
//            if (_ID == ownerID){
//                return true;
//            }
//        }
//        return false;
//    }
    public List<Restaurant> getRrestaurants(int no_prop) {

        return restaurantRepository.getAllRestaurants(no_prop);
    }
    public  Restaurant getRestaurant(int no_prop, int no_restau){
        return restaurantRepository.getRestaurant(no_prop,no_restau);
    }

//        RestaurantRespository restaurantRespository =   new RestaurantRespository();
//        restaurantRespository.addRestaurant(1000, new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));
//       return restaurantRespository.getAllRestaurants(1000);
//        System.out.print("----------------------------------------------------------------------nami----------------------------------------------------------------------");
//
//       if (isExistingOwnerID(no_prop)){
//           return restaurantRepository.getAllRestaurants(no_prop);
//        }
//       System.out.print("----------------------------------------------------------------------nami----------------------------------------------------------------------");
////       if (!isExistingOwnerID(no_prop)){
////           // createInvalidError("Invalide Owner ID");
////       }
//        return restaurantRepository.getAllRestaurants(00000);
    }

