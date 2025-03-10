package ca.ulaval.glo2003.resource;

import static ca.ulaval.glo2003.domain.error.ErrorType.*;
import static ca.ulaval.glo2003.util.Constante.*;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.api.assemblers.request.RestaurantRequestAssembler;
import ca.ulaval.glo2003.api.exceptionMapper.InvalidParameterExceptionMapper;
import ca.ulaval.glo2003.api.exceptionMapper.MissingParameterExceptionMapper;
import ca.ulaval.glo2003.api.exceptionMapper.NotFoundExceptionMapper;
import ca.ulaval.glo2003.api.request.HoursRequest;
import ca.ulaval.glo2003.api.request.ReservationDurationRequest;
import ca.ulaval.glo2003.api.request.RestaurantRequest;
import ca.ulaval.glo2003.api.resource.RestaurantResource;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.domain.entity.Hours;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.error.Error;
import ca.ulaval.glo2003.repository.RestaurantRepositoryInMemory;
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
  public static final String MISSING_OWNER_ID = "Missing owner ID.";
  public final String UN_NOM = "un nom";
  private final String RESTAURANT_ID = "10000";
  private final String OWNER_ID = "00001";
  private final LocalTime OPEN = LocalTime.of(10, 30, 45);
  private final LocalTime CLOSE = LocalTime.of(19, 30, 45);
  private final int CAPACITY = 2;
  private Hours hours;
  private RestaurantService restaurantService;

  private RestaurantRepositoryInMemory restaurantRespository;
  private RestaurantRequestAssembler restaurantRequestAssembler;
  private RestaurantDto restaurantDto;

  private RestaurantRequest restaurantRequest;
  private Response response;

  @Override
  protected Application configure() {
    restaurantRespository = new RestaurantRepositoryInMemory();
    restaurantService = new RestaurantService(restaurantRespository);
    restaurantRequestAssembler = new RestaurantRequestAssembler();
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

    restaurantRequest =
        new RestaurantRequest(
            RESTAURANT_ID,
            UN_NOM,
            CAPACITY,
            new HoursRequest(hours.getOpen(), hours.getClose()),
            new ReservationDurationRequest(70));
    restaurantDto = restaurantRequestAssembler.toDto(restaurantRequest);
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenRestaurantIsValid_thenAddRestaurantAddTheRestaurantToRepository() {

    Response response =
        target("/restaurants/")
            .request()
            .header("Owner", OWNER_ID)
            .post(Entity.json(restaurantRequest));

    String location = ((String) response.getHeaders().get("Location").get(0));
    int contentLength =
        Integer.parseInt((String) response.getHeaders().get("content-Length").get(0));

    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    assertEquals(true, location.startsWith(URL + ":" + PORT + "/restaurants/"));
    assertEquals(0, contentLength);
  }

  @Test
  void givenRestaurant_whenOwnerIdNotProvide_thenAddRestaurantReturnBodyWithMissingError() {

    response = target("/restaurants/").request().post(Entity.json(restaurantRequest));
    Error body = response.readEntity(Error.class);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_OWNER_ID);
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  void
      givenRestaurantAndOwnerId_whenOwnerIdIsNullOrEmpty_thenAddRestaurantReturnBodyWithMissingError(
          String ownerId) {

    response =
        target("/restaurants/")
            .request()
            .header("Owner", ownerId)
            .post(Entity.json(restaurantRequest));
    Error body = response.readEntity(Error.class);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_OWNER_ID);
  }

  @Test
  void givenRestaurantAndOwnerId_whenHoursIsNull_thenAddRestaurantReturnBodyWithMissingError() {

    restaurantRequest =
        new RestaurantRequest(
            RESTAURANT_ID, UN_NOM, CAPACITY, null, new ReservationDurationRequest(70));

    response =
        target("/restaurants/")
            .request()
            .header("Owner", OWNER_ID)
            .post(Entity.json(restaurantRequest));
    Error body = response.readEntity(Error.class);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_RESTAURANT_MESSAGE);
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenCapacityIsLessThanOne_thenAddRestaurantReturnBodyWithInvalidError() {

    restaurantRequest =
        new RestaurantRequest(
            RESTAURANT_ID,
            UN_NOM,
            0,
            new HoursRequest(hours.getOpen(), hours.getClose()),
            new ReservationDurationRequest(70));

    response =
        target("/restaurants/")
            .request()
            .header("Owner", OWNER_ID)
            .post(Entity.json(restaurantRequest));
    Error body = response.readEntity(Error.class);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(INVALID_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(INVALID_RESTAURANT_MESSAGE);
  }

  @Test
  void givenRestaurantAndOwnerId_whenHoursIsInvalid_thenAddRestaurantReturnBodyWithInvalidError() {

    hours.setClose(OPEN);
    hours.setOpen(CLOSE);
    restaurantRequest =
        new RestaurantRequest(
            RESTAURANT_ID,
            UN_NOM,
            CAPACITY,
            new HoursRequest(hours.getOpen(), hours.getClose()),
            new ReservationDurationRequest(70));

    response =
        target("/restaurants/")
            .request()
            .header("Owner", OWNER_ID)
            .post(Entity.json(restaurantRequest));
    Error body = response.readEntity(Error.class);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(INVALID_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(INVALID_RESTAURANT_MESSAGE);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantNotExistInRepository_thenGetRestaurantReturnBodyWithNotFoundError() {

    restaurantService.addOwnerIfNew(OWNER_ID);

    response = target("/restaurants/" + RESTAURANT_ID).request().header("Owner", OWNER_ID).get();
    Error body = response.readEntity(Error.class);
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(NOT_FOUND);
    assertThat(body.getDescription()).isEqualTo(RESTAURANT_NOT_FOUND);
  }

  @Test
  void
      givenOwnerIdAndRestaurantId_whenRestaurantInRepository_thenGetRestaurantReturnBodyRestaurant()
          throws Exception {

    restaurantService.addOwnerIfNew(OWNER_ID);
    RestaurantDto addedRestaurant = restaurantService.addRestaurant(OWNER_ID, restaurantDto);

    response =
        target("/restaurants/" + addedRestaurant.id()).request().header("Owner", OWNER_ID).get();
    Restaurant body = response.readEntity(Restaurant.class);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertThat(body.getName()).isEqualTo(restaurantRequest.getName());
    assertThat(body.getId()).isEqualTo(restaurantRequest.getId());
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenOwnerIdIsNullOrEmpty_thenGetRestaurantsReturnBodyWithMissingError()
          throws Exception {

    response = target("/restaurants/").request().header("Owner", null).get();
    Error body = response.readEntity(Error.class);
    assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    assertThat(body.getError()).isEqualTo(MISSING_PARAMETER);
    assertThat(body.getDescription()).isEqualTo(MISSING_OWNER_ID);
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenRestaurantsNotExistInRepository_thenGetRestaurantsReturnBodyWithEmptyListOfRestaurants()
          throws Exception {

    restaurantService.addOwnerIfNew(OWNER_ID);

    response = target("/restaurants/").request().header("Owner", OWNER_ID).get();
    List<Restaurant> body = response.readEntity(List.class);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertThat(body.size()).isEqualTo((0));
  }

  @Test
  void
      givenRestaurantAndOwnerId_whenRestaurantsExistInRepository_thenGetRestaurantsReturnBodyListOfRestaurants()
          throws Exception {

    restaurantService.addOwnerIfNew(OWNER_ID);
    restaurantService.addRestaurant(OWNER_ID, restaurantDto);

    response = target("/restaurants/").request().header("Owner", OWNER_ID).get();
    List<Restaurant> body = response.readEntity(List.class);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertThat(body.size()).isEqualTo(1);
  }
}
