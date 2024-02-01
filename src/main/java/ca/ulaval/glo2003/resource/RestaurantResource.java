package ca.ulaval.glo2003.resource;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.repository.RestaurantRespository;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ca.ulaval.glo2003.service.RestaurantService;

import java.security.InvalidParameterException;
import java.util.List;

import static ca.ulaval.glo2003.entity.ErrorType.*;

@Path("restaurants")
public class RestaurantResource {

@GET
public Response Restaurants (){
    RestaurantService Rrestaurants = new RestaurantService();
   // RestaurantRespository Rrestaurants = new RestaurantRespository();
//    System.out.print(Rrestaurants);
    return Response.ok(Rrestaurants).build();
}




    private RestaurantService restaurantService;
    public RestaurantResource() {
    }
}
