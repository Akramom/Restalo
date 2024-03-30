package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.entity.Owner;
import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import java.time.LocalDate;
import java.util.List;

public interface IRestaurantRepository {
  List<Owner> getOwners();

  Owner getOwner(String ownerId) throws NotFoundException;

  Restaurant addRestaurant(String ownerId, Restaurant restaurant) throws NotFoundException;

  Owner addOwner(String ownerId);

  Restaurant getOwnerRestaurantById(String ownerId, String restaurantId) throws NotFoundException;

  Restaurant getRestaurantById(String restaurantId) throws NotFoundException;

  List<Restaurant> getAllOwnerRestaurants(String ownerId) throws NotFoundException;

  List<Restaurant> getAllRestaurants();

  Reservation addReservation(Reservation reservation, String restaurantId) throws NotFoundException;

  Reservation getReservationByNumber(String reservationNumer) throws NotFoundException;

  Restaurant getRestaurantByReservationNumber(String number) throws NotFoundException;

  void deleteOwnerRestaurantById(String ownerId, String restaurantId) throws NotFoundException;

  List<Availability> getAvailabilities(String restaurantId, LocalDate date)
      throws NotFoundException;

  void updateAvailability(Availability updatedAvailability);

  void deleteReservation(String reservationNumber) throws NotFoundException;
}
