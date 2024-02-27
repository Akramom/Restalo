package ca.ulaval.glo2003.resource;

import static ca.ulaval.glo2003.entity.ErrorType.INVALID_PARAMETER;
import static ca.ulaval.glo2003.entity.ErrorType.MISSING_PARAMETER;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

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

  public RestaurantResource() {
    restaurantService = new RestaurantService();
  }

  @POST
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addRestaurant(@HeaderParam("Owner") String ownerId, Restaurant restaurantDto)
      throws Exception {
    try {
      restaurantService.verifyOwnerId(ownerId);
    } catch (InvalidParameterException inexistingOwner) {
      restaurantService.addNewOwner(ownerId);
    }
    restaurantService.verifyRestaurantParameter(restaurantDto);
    restaurantDto.generateId();
    Restaurant restaurant =
        new Restaurant(
            restaurantDto.getName(),
            restaurantDto.getCapacity(),
            restaurantDto.getHours(),
            restaurantDto.getReservationDuration());
    restaurantService.addRestaurant(ownerId, restaurant);

    return Response.created(URI.create("http://localhost:8080/restaurants/" + restaurant.getId()))
        .build();
  }

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOwnerRestaurants(@HeaderParam("Owner") String ownerId) throws Exception {

    restaurantService.verifyOwnerId(ownerId);

    List<Restaurant> restaurants = restaurantService.getAllRestaurantsOfOwner(ownerId);

    if (restaurants.isEmpty()) {
      return Response.status(NOT_FOUND)
          .entity(new Error(MISSING_PARAMETER, "No restaurants found for the owner."))
          .build();
    }

    return Response.ok(restaurants).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurant(
      @HeaderParam("Owner") String ownerId, @PathParam("id") String restaurantId)
      throws MissingParameterException, InvalidParameterException {

    restaurantService.verifyOwnerId(ownerId);

    Restaurant restaurant =
        restaurantService.getRestaurantByOwnerAndRestaurantId(ownerId, restaurantId);

    if (restaurant == null) {
      return Response.status(NOT_FOUND)
          .entity(
              new Error(MISSING_PARAMETER, "Restaurant not found or does not belong to the owner."))
          .build();
    }

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
    reservation.setDurationInMin(restaurant.getReservationDuration());
    restaurantService.verifyValidReservationParameter(restaurant, reservation);
    restaurantService.addReservationToRestaurant(reservation, restaurant);
    return Response.created(URI.create("http://localhost:8080/restaurants/" + reservation.getId()))
        .build();
  }
}
