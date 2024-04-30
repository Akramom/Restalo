package ca.ulaval.glo2003.repository;

class RestaurantRepositoryInMemoryTest extends AbstractRestaurantRepositoryTest {

  @Override
  RestaurantRepository createPersistence() {
    return new RestaurantRepositoryInMemory();
  }
}
