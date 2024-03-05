package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.response.reservation.ReservationPartialResponse;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantPartialResponse;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantResponse;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Util;
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
  public Response addRestaurant(@HeaderParam("Owner") String ownerId, RestaurantDto restaurantDto)
      throws Exception {
    restaurantService.verifyOwnerId(ownerId);
    restaurantService.verifyRestaurantParameter(restaurantDto);
    RestaurantResponse restaurantResponse = restaurantService.addRestaurant(ownerId, restaurantDto);

    return Response.created(
            URI.create("http://localhost:8080/restaurants/" + restaurantResponse.getId()))
        .build();
  }

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurants(@HeaderParam("Owner") String ownerId) throws Exception {

    restaurantService.verifyOwnerId(ownerId);

    List<RestaurantResponse> restaurants = restaurantService.getAllRestaurantsOfOwner(ownerId);

    return Response.ok(restaurants).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurant(
      @HeaderParam("Owner") String ownerId, @PathParam("id") String restaurantId) throws Exception {

    restaurantService.verifyOwnerId(ownerId);

    RestaurantPartialResponse restaurantResponse =
        restaurantService.getRestaurantByIdOfOwner(ownerId, restaurantId);

    return Response.ok(restaurantResponse).build();
  }

  @POST
  @Path("/{id}/reservations")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addReservation(
      @PathParam("id") String restaurantId, ReservationDto reservationDto)
      throws MissingParameterException, InvalidParameterException, NotFoundException {

    restaurantService.verifyExistRestaurant(restaurantId);
    restaurantService.verifyEmptyReservationParameter(reservationDto);

    int reservationDuration = restaurantService.getRestaurantReservationDuration(restaurantId);
    reservationDto.setStartTime(Util.ajustStartTimeToNext15Min(reservationDto.getStartTime()));
    reservationDto.setEndTime(Util.setEndTime(reservationDto.getStartTime(), reservationDuration));

    restaurantService.verifyValidReservationParameter(restaurantId, reservationDto);
    ReservationPartialResponse reservationPartialResponse =
        restaurantService.addReservation(reservationDto, restaurantId);
    return Response.created(
            URI.create(
                "http://localhost:8080/reservations/" + reservationPartialResponse.getNumber()))
        .build();
  }
}
