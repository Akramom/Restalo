package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.Proprietaire;
import ca.ulaval.glo2003.entity.Restaurant;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRespository {

    private List<Proprietaire> proprietaires;


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

    public Restaurant addRestaurant(int _noProprietaire, Restaurant _restaurant) {

        proprietaires.stream().filter(p -> p.getNoProprietaire() == _noProprietaire)
                .toList()
                .get(0)
                .getRestaurants()
                .add(_restaurant);

        return _restaurant;
    }
}
