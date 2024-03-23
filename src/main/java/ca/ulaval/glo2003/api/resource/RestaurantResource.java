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
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Constante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
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
      @HeaderParam("Owner")
          @NotEmpty(message = Constante.MISSING_OWNER_ID)
          @NotNull(message = Constante.MISSING_OWNER_ID)
          String ownerId,
      @Valid RestaurantRequest restaurantRequest)
      throws Exception {
    RestaurantDto restaurantDto = this.restaurantRequestAssembler.toDto(restaurantRequest);

    RestaurantDto addedRestaurant = restaurantService.addRestaurant(ownerId, restaurantDto);

    return Response.created(URI.create("http://localhost:8080/restaurants/" + addedRestaurant.id()))
        .build();
  }

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurants(
      @HeaderParam("Owner")
          @NotEmpty(message = Constante.MISSING_OWNER_ID)
          @NotNull(message = Constante.MISSING_OWNER_ID)
          String ownerId)
      throws Exception {
    List<RestaurantDto> restaurantDtos = restaurantService.getAllRestaurantsOfOwner(ownerId);

    List<OwnerRestaurantResponse> restaurantResponses =
        restaurantDtos.stream().map(this.restaurantResponseAssembler::fromDto).toList();

    return Response.ok(restaurantResponses).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurant(
      @HeaderParam("Owner")
          @NotEmpty(message = Constante.MISSING_OWNER_ID)
          @NotNull(message = Constante.MISSING_OWNER_ID)
          String ownerId,
      @PathParam("id") String restaurantId)
      throws Exception {
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
      @PathParam("id") String restaurantId, @Valid ReservationRequest reservationRequest)
      throws MissingParameterException, InvalidParameterException, NotFoundException {

    ReservationDto reservationDto = this.reservationRequestAssembler.toDto(reservationRequest);

    ReservationDto addedReservation =
        restaurantService.addReservation(reservationDto, restaurantId);
    return Response.created(
            URI.create("http://localhost:8080/reservations/" + addedReservation.getNumber()))
        .build();
  }
}
