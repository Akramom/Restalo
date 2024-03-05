package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.response.reservation.ReservationResponse;
import ca.ulaval.glo2003.application.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reservations")
public class ReservationResource {

  private RestaurantService restaurantService;

  public ReservationResource(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @GET
  @Path("/{reservationNumber}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReservation(@PathParam("reservationNumber") String reservationNumber)
      throws Exception {

    restaurantService.verifyReservationNumber(reservationNumber);

    ReservationResponse reservationResponse =
        restaurantService.getReservationByNumber(reservationNumber);

    return Response.ok(reservationResponse).build();
  }
}
