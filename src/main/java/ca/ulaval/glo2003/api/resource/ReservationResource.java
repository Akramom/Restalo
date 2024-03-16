package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.assemblers.response.ReservationResponseAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reservations")
public class ReservationResource {

  private RestaurantService restaurantService;
  private ReservationResponseAssembler reservationResponseAssembler;

  public ReservationResource(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
    this.reservationResponseAssembler = new ReservationResponseAssembler();
  }

  @GET
  @Path("/{reservationNumber}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReservation(@PathParam("reservationNumber") String reservationNumber)
      throws Exception {
    ReservationDto reservationDto = restaurantService.getReservationByNumber(reservationNumber);
    return Response.ok(
            this.reservationResponseAssembler.fromDto(
                reservationDto, reservationDto.getRestaurantDto()))
        .build();
  }
}
