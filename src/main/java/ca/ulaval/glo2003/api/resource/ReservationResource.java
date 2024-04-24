package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.assemblers.request.ReservationRequestAssembler;
import ca.ulaval.glo2003.api.assemblers.response.ReservationResponseAssembler;
import ca.ulaval.glo2003.api.request.UpdateReservationRequest;
import ca.ulaval.glo2003.application.dtos.ReservationDto;
import ca.ulaval.glo2003.application.dtos.UpdateReservationDto;
import ca.ulaval.glo2003.application.service.ReservationService;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import ca.ulaval.glo2003.util.Constante;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reservations")
public class ReservationResource {

  private ReservationService reservationService;
  private ReservationResponseAssembler reservationResponseAssembler;
  private ReservationRequestAssembler reservationRequestAssembler;

  public ReservationResource(ReservationService reservationService) {
    this.reservationService = reservationService;
    this.reservationResponseAssembler = new ReservationResponseAssembler();
    this.reservationRequestAssembler = new ReservationRequestAssembler();
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

  @PUT
  @Path("/update")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateReservation(
      @NotEmpty(message = Constante.MISSING_RESERVATION_NUMBER)
          @NotNull(message = Constante.MISSING_RESERVATION_NUMBER)
          @QueryParam("reservationNumber")
          String reservationNumber,
      @QueryParam("customerEmail")
          @NotEmpty(message = Constante.MISSING_EMAIL)
          @NotNull(message = Constante.MISSING_EMAIL)
          String customerEmail,
      UpdateReservationRequest updateReservationRequest)
      throws NotFoundException, InvalidParameterException {

    UpdateReservationDto updateReservationDto =
        reservationRequestAssembler.toDto(updateReservationRequest);

    if (!reservationService.updateReservation(
        reservationNumber, customerEmail, updateReservationDto))
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("nombre de place insuffisant pour la reservation")
          .build();

    return Response.noContent().build();
  }
}
