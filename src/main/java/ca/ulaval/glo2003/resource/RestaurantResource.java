package ca.ulaval.glo2003.resource;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import static ca.ulaval.glo2003.entity.ErrorType.MISSING_PARAMETER;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

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

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnerRestaurants(@HeaderParam("Owner") String ownerId) {
        Error error = restaurantService.verifyOwnerID(ownerId);
        if (error != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        List<Restaurant> restaurants = restaurantService.getAllOwnerRestaurants(ownerId);
        if (restaurants.isEmpty()) {
            return Response.status(NOT_FOUND).entity(new Error(MISSING_PARAMETER, "No restaurants found for the owner.")).build();
        }

        return Response.ok(restaurants).build();
    }

    @GET
    @Path("/{restaurantId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurant(@HeaderParam("Owner") String ownerId, @PathParam("restaurantId") String restaurantId) {
        Error ownerError = restaurantService.verifyOwnerID(ownerId);
        if (ownerError != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ownerError).build();
        }

        Restaurant restaurant = restaurantService.getOwnerRestaurant(ownerId, restaurantId);
        if (restaurant == null) {
            return Response.status(NOT_FOUND).entity(new Error(MISSING_PARAMETER, "Restaurant not found or does not belong to the owner.")).build();
        }

        return Response.ok(restaurant).build();
    }

}
