package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.assemblers.request.ReservationRequestAssembler;
import ca.ulaval.glo2003.api.assemblers.request.RestaurantRequestAssembler;
import ca.ulaval.glo2003.api.assemblers.response.RestaurantResponseAssembler;
import ca.ulaval.glo2003.api.request.ReservationRequest;
import ca.ulaval.glo2003.api.request.RestaurantRequest;
import ca.ulaval.glo2003.api.response.restaurant.OwnerRestaurantResponse;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import ca.ulaval.glo2003.domain.entity.Availability;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/restaurants")
public class RestaurantResource {

  private final RestaurantService restaurantService;
  private final RestaurantRequestAssembler restaurantRequestAssembler;
  private final RestaurantResponseAssembler restaurantResponseAssembler;
  private final ReservationRequestAssembler reservationRequestAssembler;

  public RestaurantResource(RestaurantService restaurantService) {

    this.restaurantService = restaurantService;
    this.restaurantRequestAssembler = new RestaurantRequestAssembler();
    this.reservationRequestAssembler = new ReservationRequestAssembler();
    this.restaurantResponseAssembler = new RestaurantResponseAssembler();
  }

  @POST
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addRestaurant(
      @HeaderParam("Owner") String ownerId, RestaurantRequest restaurantRequest) throws Exception {

    RestaurantDto restaurantDto = this.restaurantRequestAssembler.toDto(restaurantRequest);

    RestaurantDto addedRestaurant = restaurantService.addRestaurant(ownerId, restaurantDto);

    return Response.created(URI.create("http://localhost:8080/restaurants/" + addedRestaurant.id()))
        .build();
  }

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurants(@HeaderParam("Owner") String ownerId) throws Exception {

    List<RestaurantDto> restaurantDtos = restaurantService.getAllRestaurantsOfOwner(ownerId);

    List<OwnerRestaurantResponse> restaurantResponses =
        restaurantDtos.stream().map(this.restaurantResponseAssembler::fromDto).toList();

    return Response.ok(restaurantResponses).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurant(
      @HeaderParam("Owner") String ownerId, @PathParam("id") String restaurantId) throws Exception {

    RestaurantDto restaurantDto = restaurantService.getRestaurantByIdOfOwner(ownerId, restaurantId);

    OwnerRestaurantResponse restaurantResponse =
        this.restaurantResponseAssembler.fromDto(restaurantDto);

    return Response.ok(restaurantResponse).build();
  }

  @POST
  @Path("/{id}/reservations")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addReservation(
      @PathParam("id") String restaurantId, ReservationRequest reservationRequest)
      throws MissingParameterException, InvalidParameterException, NotFoundException {

    ReservationDto reservationDto = this.reservationRequestAssembler.toDto(reservationRequest);

    ReservationDto addedReservation =
        restaurantService.addReservation(reservationDto, restaurantId);
    return Response.created(
            URI.create("http://localhost:8080/reservations/" + addedReservation.getNumber()))
        .build();
  }
  @GET
  @Path("/{id}/availabilities")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAvailabilities(@PathParam("id") String restaurantId, @QueryParam("date") String dateStr) throws NotFoundException {
    LocalDate date;
    try {
      date = restaurantService.validateAndParseDate(dateStr);
    } catch (IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST)
              .entity(new InvalidParameterException("INVALID_PARAMETER"))
              .build();
    }
    List<Availability> availabilities = restaurantService.calculateAvailabilities(restaurantId, date);
    return Response.ok(availabilities).build();
  }

}


