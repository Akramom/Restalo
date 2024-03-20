package ca.ulaval.glo2003.repository;

import ca.ulaval.glo2003.domain.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public interface IRestaurantRepositoryTest {
  @BeforeEach
  void setUp();

  @Test
  void givenOwnerIdAndRestaurantId_WhenAddRestaurant_ThenRestaurantIsAddInRepository()
      throws NotFoundException;

  @Test
  void
      givenOwnerIdAndRestaurantId_whenGetRestaurantAndRestaurantIsInRepository_ThenReturnRestaurant()
          throws NotFoundException;

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotInRepository_ThenGetRestaurantShouldThrowNotFoundError()
          throws NotFoundException;

  @Test
  void givenAnOwnerId_whenGetAllOwnerRestaurants_thenReturnListOfRestaurantsForTheOwner()
      throws NotFoundException;

  @Test
  void givenRestaurantIdInRepository_whenGetRestaurantById_thenReturnsRestaurant()
      throws NotFoundException;

  @Test
  void givenRestaurantIdAndReservation_whenAddReservation_thenReservationIsAddedToRestaurant()
      throws NotFoundException;

  @Test
  void givenInvalidRestaurantId_whenAddReservation_thenThrowsNotFoundException()
      throws NotFoundException;

  @Test
  void getReservationByNumber_WhenExists_ReturnsReservation() throws NotFoundException;

  @Test
  void getReservationByNumber_WhenNotExists_ThrowsNotFoundException() throws NotFoundException;
}
