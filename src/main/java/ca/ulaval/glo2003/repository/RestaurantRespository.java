package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.Proprietaire;
import ca.ulaval.glo2003.entity.Restaurant;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRespository {

    private static List<Proprietaire> proprietaires;


    public RestaurantRespository() {
        proprietaires = new ArrayList<>();
        init();
    }

    public void init() {
        Proprietaire proprietaire1 = new Proprietaire("alice", "gill", "418-999-999");
        Proprietaire proprietaire2 = new Proprietaire("bob", "samuel", "581-999-999");
        Proprietaire proprietaire3 = new Proprietaire("franck", "poulin", "581-988-999");


        proprietaires.add(proprietaire1);
        proprietaires.add(proprietaire2);
        proprietaires.add(proprietaire3);
    }

    public static boolean proprietaireExiste(int noProprietaire) {

        return proprietaires.stream().anyMatch(proprietaire -> proprietaire.getNoProprietaire() == noProprietaire);
    }
    public Restaurant addRestaurant(int _noProprietaire, Restaurant _restaurant) {

        proprietaires.stream().filter(p -> p.getNoProprietaire() == _noProprietaire)
                .toList()
                .get(0)
                .getRestaurants()
                .add(_restaurant);

        return _restaurant;
    }



    // Methode dans le repository pour retrouver un restaurant par son id
    public Restaurant getRestaurant(int  _noProprietaire, int _noRestaurant){
        Restaurant restaurant = proprietaires.stream().filter(p-> p.getNoProprietaire()==_noProprietaire)
                .toList().get(0).getRestaurants().stream()
                .filter(r->r.getNoRestaurant()==_noRestaurant).findFirst().orElse(null);
        return restaurant;
    }

    public List<Restaurant> getAllRestaurants(int  _noProprietaire){
        List<Restaurant> restaurants = proprietaires.stream().filter(p-> p.getNoProprietaire()==_noProprietaire)
                .toList().get(0).getRestaurants();
        return restaurants;
    }



    public static void main(String[] args) {
        RestaurantRespository restaurantRespository =   new RestaurantRespository();
        System.out.println(restaurantRespository.proprietaires);

        restaurantRespository.addRestaurant(1000, new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));
        restaurantRespository.addRestaurant(1000, new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));
        restaurantRespository.addRestaurant(1000, new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));
        restaurantRespository.addRestaurant(1001, new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));

        System.out.println(restaurantRespository.proprietaires);

        System.out.println(restaurantRespository.getAllRestaurants(1002));

    }


}
