package ca.ulaval.glo2003.api.resource;

import ca.ulaval.glo2003.api.assemblers.request.SearchRequestAssembler;
import ca.ulaval.glo2003.api.assemblers.response.RestaurantResponseAssembler;
import ca.ulaval.glo2003.api.request.SearchRequest;
import ca.ulaval.glo2003.api.response.restaurant.RestaurantResponse;
import ca.ulaval.glo2003.application.dtos.RestaurantDto;
import ca.ulaval.glo2003.application.dtos.SearchDto;
import ca.ulaval.glo2003.application.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/search")
public class SearchResource {
  private RestaurantService restaurantService;
  private RestaurantResponseAssembler restaurantResponseAssembler;

  public SearchResource(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
    this.restaurantResponseAssembler = new RestaurantResponseAssembler();
  }

  @POST
  @Path("/restaurants")
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchRestaurant(SearchRequest searchRequest) throws Exception {

    SearchDto searchDto = new SearchRequestAssembler().toDto(searchRequest);

    List<RestaurantDto> restaurantDtos = restaurantService.searchRestaurant(searchDto);

    List<RestaurantResponse> restaurantResponses =
        restaurantDtos.stream()
            .map(this.restaurantResponseAssembler::restaurantResponseFromDto)
            .toList();

    return Response.ok().entity(restaurantResponses).build();
  }
}
