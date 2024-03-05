package ca.ulaval.glo2003.resource;

import static ca.ulaval.glo2003.domain.error.ErrorType.*;
import static ca.ulaval.glo2003.util.Constante.RESTAURANT_NOT_FOUND;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.api.resource.RestaurantResource;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantResponse;
import ca.ulaval.glo2003.application.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.domain.entity.Hours;
import ca.ulaval.glo2003.domain.entity.ReservationDuration;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.error.Error;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.domain.exception.exceptionMapper.InvalidParameterExceptionMapper;
import ca.ulaval.glo2003.domain.exception.exceptionMapper.MissingParameterExceptionMapper;
import ca.ulaval.glo2003.domain.exception.exceptionMapper.NotFoundExceptionMapper;
import ca.ulaval.glo2003.repository.RestaurantRepository;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import java.time.LocalTime;
import java.util.List;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

class RestaurantResourceIntegrationTest extends JerseyTest {

  public static final String MISSING_RESTAURANT_MESSAGE = "Missing restaurant parameter.";
  public static final String INVALID_RESTAURANT_MESSAGE = "Invalid restaurant parameter.";
  public static final String NOT_FOUND_MESSAGE = "No restaurant found for the owner.";
  public static final String MISSING_OWNER_ID = "Missing owner ID.";
  public final String UN_NOM = "un nom";
  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final int CAPACITY = 2;
  private Hours hours;
  private RestaurantService restaurantService;

  private RestaurantRespository restaurantRespository;
  ;
  private Restaurant restaurant;
  private Response response;

  @Override
  protected Application configure() {
    restaurantRespository = new RestaurantRepository();
    restaurantService = new RestaurantService(restaurantRespository);
    restaurantAssembler = new RestaurantAssembler();
    return new ResourceConfig()
        .register(new RestaurantResource(restaurantService))
        .register(MissingParameterExceptionMapper.class)
        .register(InvalidParameterExceptionMapper.class)
        .register(NotFoundExceptionMapper.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();

    hours = new Hours(OPEN, CLOSE);

    restaurant = new Restaurant(UN_NOM, CAPACITY, hours, new ReservationDuration(70));
    restaurantDto = restaurantAssembler.toDto(restaurant);
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenRestaurantIsValid_ThenAddRestaurantAddTheRestaurantToRepository() {

    Response response =
        target("/restaurants/").request().header("Owner", OWNER_ID).post(Entity.json(restaurant));

    String location = ((String) response.getHeaders().get("Location").get(0));
    int contentLength =
        Integer.parseInt((String) response.getHeaders().get("content-Length").get(0));

    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    assertEquals(true, location.startsWith("http://localhost:8080/restaurants/"));
    assertEquals(0, contentLength);
  }

  @Test
  void givenRestaurant_whenOwnerIdNotProvide_ThenAddRestaurantReturnBodyWithMissingError() {

    response = target("/restaurants/").request().post(Entity.json(restaurant));
    Error body = response.readEntity(Error.class);
    System.out.println(body);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_OWNER_ID);
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  void
      givenRestaurantAndOwnerId_whenOwnerIdIsNullOrEmpty_ThenAddRestaurantReturnBodyWithMissingError(
          String ownerId) {

    response =
        target("/restaurants/").request().header("Owner", ownerId).post(Entity.json(restaurant));
    Error body = response.readEntity(Error.class);
    System.out.println(body);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_OWNER_ID);
  }

  @Test
  void givenRestaurantAndOwnerId_WhenHoursIsNull_ThenAddRestaurantReturnBodyWithMissingError() {

    restaurant.setHours(null);
    response =
        target("/restaurants/").request().header("Owner", OWNER_ID).post(Entity.json(restaurant));
    Error body = response.readEntity(Error.class);
    System.out.println(body);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_RESTAURANT_MESSAGE);
  }

  @Test
  void
      givenRestaurantAndOwnerId_WhenCapacityIsLessThanOne_ThenAddRestaurantReturnBodyWithInvalidError() {

    restaurant.setCapacity(0);
    response =
        target("/restaurants/").request().header("Owner", OWNER_ID).post(Entity.json(restaurant));
    Error body = response.readEntity(Error.class);
    System.out.println(body);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(INVALID_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(INVALID_RESTAURANT_MESSAGE);
  }

  @Test
  void givenRestaurantAndOwnerId_WhenHoursIsInvalid_ThenAddRestaurantReturnBodyWithInvalidError() {

    hours.setClose(OPEN);
    hours.setOpen(CLOSE);
    restaurant.setHours(hours);

    response =
        target("/restaurants/").request().header("Owner", OWNER_ID).post(Entity.json(restaurant));
    Error body = response.readEntity(Error.class);
    System.out.println(body);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(INVALID_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(INVALID_RESTAURANT_MESSAGE);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotExistInRepository_thenGetRestaurantReturnBodyWithNotFoundError() {

    restaurantService.addNewOwner(OWNER_ID);

    response = target("/restaurants/" + RESTAURANT_ID).request().header("Owner", OWNER_ID).get();
    Error body = response.readEntity(Error.class);
    System.out.println(body);
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(NOT_FOUND);
    assertThat(body.getDescription()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantInRepository_thenGetRestaurantReturnBodyRestaurant()
          throws NotFoundException {

    restaurantService.addNewOwner(OWNER_ID);
    RestaurantResponse restaurantResponse =
        restaurantService.addRestaurant(OWNER_ID, restaurantDto);

    response =
        target("/restaurants/" + restaurantResponse.getId())
            .request()
            .header("Owner", OWNER_ID)
            .get();
    Restaurant body = response.readEntity(Restaurant.class);
    System.out.println(body);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertThat(body.getName()).isEqualTo(restaurant.getName());
    assertThat(body.getId()).isEqualTo(restaurant.getId());
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenOwnerIdIsNullOrEmpty_thenGetRestaurantsReturnBodyWithMissingError()
          throws Exception {

    response = target("/restaurants/").request().header("Owner", null).get();
    Error body = response.readEntity(Error.class);
    System.out.println(body);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_OWNER_ID);
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenRestaurantsNotExistInRepository_thenGetRestaurantsReturnBodyWithEmptyListOfRestaurants()
          throws Exception {

    restaurantService.addNewOwner(OWNER_ID);

    response = target("/restaurants/").request().header("Owner", OWNER_ID).get();
    List<Restaurant> body = response.readEntity(List.class);
    System.out.println(body);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertThat(body.size()).isEqualTo((0));
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenRestaurantsExistInRepository_thenGetRestaurantsReturnBodyListOfRestaurants()
          throws Exception {

    restaurantService.addNewOwner(OWNER_ID);
    restaurantService.addRestaurant(OWNER_ID, restaurantDto);

    response = target("/restaurants/").request().header("Owner", OWNER_ID).get();
    List<Restaurant> body = response.readEntity(List.class);
    System.out.println(body);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertThat(body.size()).isEqualTo(1);
  }
}
