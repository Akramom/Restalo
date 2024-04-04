package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.assemblers.response.ReservationResponseAssembler;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.service.ReservationService;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reservations")
public class ReservationResource {

  private ReservationService reservationService;
  private ReservationResponseAssembler reservationResponseAssembler;

  public ReservationResource(ReservationService reservationService) {
    this.reservationService = reservationService;
    this.reservationResponseAssembler = new ReservationResponseAssembler();
  }

  @GET
  @Path("/{reservationNumber}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReservation(@PathParam("reservationNumber") String reservationNumber)
      throws Exception {
    ReservationDto reservationDto = reservationService.getReservationByNumber(reservationNumber);
    return Response.ok(
            this.reservationResponseAssembler.fromDto(
                reservationDto, reservationDto.getRestaurantDto()))
        .build();
  }

  @DELETE
  @Path("/{reservationNumber}")
  public Response deleteReservation(@PathParam("reservationNumber") String reservationNumber)
      throws NotFoundException {

    reservationService.deleteReservation(reservationNumber);

    return Response.noContent().build();
  }
}
