package ca.ulaval.glo2003.resource;
import ca.ulaval.glo2003.repository.RestaurantRespository;
import jakarta.ws.rs.core.Response;
import ca.ulaval.glo2003.service.RestaurantService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
@Path("all-res")
public class RestaurantResource {
@GET
public Response Restaurants (){
    RestaurantRespository rr = new RestaurantRespository();
    return Response.ok(rr.getAllRestaurants(1000)).build();
}

    private RestaurantService restaurantService;
    public RestaurantResource() {
    }
}
