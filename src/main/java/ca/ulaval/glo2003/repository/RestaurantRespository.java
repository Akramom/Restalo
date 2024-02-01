package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.Owner;
import ca.ulaval.glo2003.entity.Restaurant;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRespository {

    private static List<Owner> owners;
    private List<Restaurant> restaurants;


    public RestaurantRespository() {
        owners = new ArrayList<>();
        restaurants = new ArrayList<>();
        init();
    }

    public List<Owner> getOwner(){
        return owners;
    }

    public void init() {
        Owner owner1 = new Owner("1000","alice", "gill", "418-999-999");
        Owner owner2 = new Owner("2000","bob", "samuel", "581-999-999");
        Owner owner3 = new Owner("3000","franck", "poulin", "581-988-999");


        owners.add(owner1);
        owners.add(owner2);
        owners.add(owner3);
    }

    public Restaurant addRestaurant(String _noOwner, Restaurant _restaurant) {

        owners.stream().filter(p -> p.getNoOwner().equals(_noOwner))
                .toList()
                .get(0)
                .getRestaurants()
                .add(_restaurant);
        restaurants.add(_restaurant);

        return _restaurant;
    }

    public void addOwner(String noOwner){
        Owner owner = new Owner("Doe", "John", "418-222-2222");
        owner.setNoOwner(noOwner);
        owners.add(owner);
    }



    // Methode dans le repository pour retrouver un restaurant par son id
    public Restaurant getRestaurant(String  _noOwner, String _noRestaurant){
        Restaurant restaurant = owners.stream().filter(p-> p.getNoOwner().equals(_noOwner))
                .toList().get(0).getRestaurants().stream()
                .filter(r->r.getNoRestaurant().equals(_noRestaurant)).findFirst().orElse(null);
        return restaurant;
    }

    public List<Restaurant> getAllRestaurants(String  _noOwner){

        List<Restaurant> ownerRestaurants = owners.stream().filter(p-> p.getNoOwner().equals(_noOwner))

                .toList().get(0).getRestaurants();
        return ownerRestaurants;
    }

    public Boolean noRestaurantExists(String noRestaurant){
        for (Restaurant restaurant: restaurants){
            if(restaurant.getNoRestaurant() == noRestaurant){
                return true;
            }
        }
        return false;
    }



    public static void main(String[] args) {
        RestaurantRespository restaurantRespository =   new RestaurantRespository();
        System.out.println(restaurantRespository.owners);

        restaurantRespository.addRestaurant("1000", new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));
        restaurantRespository.addRestaurant("1000", new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));
        restaurantRespository.addRestaurant("1000", new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));
        restaurantRespository.addRestaurant("1001", new Restaurant("Carthage", 20, new Hours( LocalTime.now(), LocalTime.now().plusHours(10))));

        System.out.println(restaurantRespository.owners);

        System.out.println(restaurantRespository.getAllRestaurants("1002"));

    }


}
