package ca.ulaval.glo2003.api.resource;

import static ca.ulaval.glo2003.util.Constante.PORT;
import static ca.ulaval.glo2003.util.Constante.URL;

import ca.ulaval.glo2003.api.assemblers.request.ReservationRequestAssembler;
import ca.ulaval.glo2003.api.assemblers.request.RestaurantRequestAssembler;
import ca.ulaval.glo2003.api.assemblers.response.AvailabilityResponseAssembler;
import ca.ulaval.glo2003.api.assemblers.response.ReservationResponseAssembler;
import ca.ulaval.glo2003.api.assemblers.response.RestaurantResponseAssembler;
import ca.ulaval.glo2003.api.request.ReservationRequest;
import ca.ulaval.glo2003.api.request.RestaurantRequest;
import ca.ulaval.glo2003.api.request.UpdateRestaurantRequest;
import ca.ulaval.glo2003.api.response.restaurant.OwnerRestaurantResponse;
import ca.ulaval.glo2003.application.dtos.*;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/restaurants")
public class RestaurantResource {

  private final RestaurantService restaurantService;
  private final RestaurantRequestAssembler restaurantRequestAssembler;
  private final RestaurantResponseAssembler restaurantResponseAssembler;
  private final ReservationRequestAssembler reservationRequestAssembler;
  private final AvailabilityResponseAssembler availabilityResponseAssembler;
  private final ReservationResponseAssembler reservationResponseAssembler;

  public RestaurantResource(RestaurantService restaurantService) {

    this.restaurantService = restaurantService;
    this.restaurantRequestAssembler = new RestaurantRequestAssembler();
    this.reservationRequestAssembler = new ReservationRequestAssembler();
    this.restaurantResponseAssembler = new RestaurantResponseAssembler();
    this.availabilityResponseAssembler = new AvailabilityResponseAssembler();
    this.reservationResponseAssembler = new ReservationResponseAssembler();
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

    return Response.created(URI.create(URL + ":" + PORT + "/restaurants/" + addedRestaurant.id()))
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
      throws InvalidParameterException, NotFoundException, MissingParameterException {

    ReservationDto reservationDto = this.reservationRequestAssembler.toDto(reservationRequest);

    ReservationDto addedReservation =
        restaurantService.addReservation(reservationDto, restaurantId);
    return Response.created(
            URI.create(URL + ":" + PORT + "/reservations/" + addedReservation.getNumber()))
        .build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteRestaurant(
      @HeaderParam("Owner")
          @NotEmpty(message = Constante.MISSING_OWNER_ID)
          @NotNull(message = Constante.MISSING_OWNER_ID)
          String ownerId,
      @PathParam("id") String restaurantId)
      throws NotFoundException {

    restaurantService.deleteRestaurantIfOwner(restaurantId, ownerId);

    return Response.noContent().build();
  }

  @GET
  @Path("/{id}/availabilities")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAvailabilities(
      @NotEmpty(message = Constante.MISSING_RESTAURANT_ID)
          @NotNull(message = Constante.MISSING_RESTAURANT_ID)
          @PathParam("id")
          String restaurantId,
      @QueryParam("date")
          @NotEmpty(message = Constante.MISSING_DATE)
          @NotNull(message = Constante.MISSING_DATE)
          String dateStr)
      throws NotFoundException, DateTimeParseException {

    LocalDate date = LocalDate.parse(dateStr);

    List<AvailabilityDto> availabilities =
        restaurantService.getAvailabilityService().getAvailabilities(restaurantId, date);

    return Response.ok(availabilities.stream().map(availabilityResponseAssembler::fromDto).toList())
        .build();
  }

  @GET
  @Path("/{id}/reservations")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findReservation(
      @HeaderParam("Owner")
          @NotEmpty(message = Constante.MISSING_OWNER_ID)
          @NotNull(message = Constante.MISSING_OWNER_ID)
          String ownerId,
      @NotEmpty(message = Constante.MISSING_RESTAURANT_ID)
          @NotNull(message = Constante.MISSING_RESTAURANT_ID)
          @PathParam("id")
          String restaurantId,
      @QueryParam("date") String date,
      @QueryParam("customerName") String customerName)
      throws NotFoundException, DateTimeParseException {

    List<ReservationDto> reservations =
        this.restaurantService
            .getReservationService()
            .searchReservation(ownerId, restaurantId, date, customerName);

    return Response.ok(reservations.stream().map(reservationResponseAssembler::fromDto).toList())
        .build();
  }

  @PUT
  @Path("/`{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateRestauarant(
      @NotEmpty(message = Constante.MISSING_RESTAURANT_ID)
          @NotNull(message = Constante.MISSING_RESTAURANT_ID)
          @PathParam("id")
          String restaurantId,
      @NotEmpty(message = Constante.MISSING_OWNER_ID)
          @NotNull(message = Constante.MISSING_OWNER_ID)
          @HeaderParam("Owner")
          String ownerId,
      UpdateRestaurantRequest updateRestaurantRequest)
      throws Exception {

    UpdateRestaurantDto UpdateRestaurantDto =
        restaurantRequestAssembler.toDto(updateRestaurantRequest);

    restaurantService.updateRestaurant(ownerId, restaurantId, UpdateRestaurantDto);

    return Response.noContent().build();
  }
}
