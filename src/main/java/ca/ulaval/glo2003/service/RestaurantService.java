package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.repository.RestaurantRespository;

public class RestaurantService {

    private RestaurantRespository restaurantRespository;

    public RestaurantService() {
    }

    public Error validerParametrePresent(Restaurant newRestaurant) {

        return null;
    }

    public void addRestaurant(int noProprietaire, Restaurant newRestaurant) {
        restaurantRespository.addRestaurant(noProprietaire,newRestaurant);
    }

    public Error validerParametreValide(Restaurant newRestaurant) {
        return null;
    }
}
