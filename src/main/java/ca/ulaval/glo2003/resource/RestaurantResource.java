package ca.ulaval.glo2003.resource;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/restaurants")
public class RestaurantResource {

    private RestaurantService restaurantService;

    public RestaurantResource() {
        restaurantService = new RestaurantService();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRestaurant(@HeaderParam("Owner") String  Owner, Restaurant newRestaurant) {

        Error exist = restaurantService.verifyOwnerID(Owner);

        if (exist!= null)
            return Response.status(Response.Status.BAD_REQUEST).entity(exist).build();


        Error valid = restaurantService.verifyCreateRestaurantReq(newRestaurant);

        if (valid!=null)
            return Response.status(Response.Status.BAD_REQUEST).entity(valid).build();

        valid = restaurantService.verifyCreateRestaurantReq(newRestaurant);

        if (valid!=null)
            return Response.status(Response.Status.BAD_REQUEST).entity(valid).build();

       Restaurant restaurant= restaurantService.addRestaurant(Owner,new Restaurant(newRestaurant.getName(), newRestaurant.getCapacity(), newRestaurant.getHours()));

        return Response.created(URI.create("http://localhost:8080/api/restautant/" + restaurant.getNoRestaurant())).build();
    }


}
