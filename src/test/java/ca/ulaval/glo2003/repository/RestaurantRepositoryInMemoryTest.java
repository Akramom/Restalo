package ca.ulaval.glo2003.repository;

class RestaurantRepositoryInMemoryTest extends AbstractRestaurantRepositoryTest {

  @Override
  IRestaurantRepository createPersistence() {
    return new RestaurantRepositoryInMemory();
  }
}
