package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.response.restaurant.RestaurantPartialResponse;
import ca.ulaval.glo2003.application.dtos.SearchDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/search")
public class SearchResource {
  private RestaurantService restaurantService;

  public SearchResource(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @POST
  @Path("/restaurants")
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchResatuarnt(SearchDto searchDto) throws Exception {

    List<RestaurantPartialResponse> restaurantResponses =
        restaurantService.searchRestaurant(searchDto);

    return Response.ok().entity(restaurantResponses).build();
  }
}
