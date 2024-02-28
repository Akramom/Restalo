package ca.ulaval.glo2003.resource;

import static ca.ulaval.glo2003.entity.ErrorType.INVALID_PARAMETER;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.entity.Reservation;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.exception.MissingParameterException;
import ca.ulaval.glo2003.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/restaurants")
public class RestaurantResource {

  private RestaurantService restaurantService;

  public RestaurantResource(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @POST
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addRestaurant(@HeaderParam("Owner") String ownerId, Restaurant restaurantDto)
      throws Exception {
    restaurantService.verifyOwnerId(ownerId);

    restaurantService.verifyRestaurantParameter(restaurantDto);
    Restaurant restaurant =
        new Restaurant(
            restaurantDto.getName(),
            restaurantDto.getCapacity(),
            restaurantDto.getHours(),
            restaurantDto.getReservation());
    restaurantService.addRestaurant(ownerId, restaurant);

    return Response.created(URI.create("http://localhost:8080/restaurants/" + restaurant.getId()))
        .build();
  }

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurants(@HeaderParam("Owner") String ownerId) throws Exception {

    restaurantService.verifyOwnerId(ownerId);

    List<Restaurant> restaurants = restaurantService.getAllRestaurantsOfOwner(ownerId);

    return Response.ok(restaurants).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurant(
      @HeaderParam("Owner") String ownerId, @PathParam("id") String restaurantId) throws Exception {

    restaurantService.verifyOwnerId(ownerId);

    Restaurant restaurant = restaurantService.getRestaurantByIdOfOwner(ownerId, restaurantId);

    return Response.ok(restaurant).build();
  }

  @POST
  @Path("/{id}/reservations")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createReservation(@PathParam("id") String restaurantId, Reservation reservation)
      throws MissingParameterException, InvalidParameterException {
    Restaurant restaurant;
    try {
      restaurant = restaurantService.verifyRestaurantId(restaurantId);
    } catch (InvalidParameterException inexistentRestaurant) {
      return Response.status(404)
          .entity(new Error(INVALID_PARAMETER, inexistentRestaurant.getMessage()))
          .build();
    }
    restaurantService.verifyEmptyReservationParameter(reservation);
    reservation.setDurationInMin(restaurant.getReservation().duration());
    restaurantService.verifyValidReservationParameter(restaurant, reservation);
    restaurantService.addReservationToRestaurant(reservation, restaurant);
    return Response.created(URI.create("http://localhost:8080/restaurants/" + reservation.getId()))
        .build();
  }
}
