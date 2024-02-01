package ca.ulaval.glo2003.resource;

import ca.ulaval.glo2003.entity.ErrorType;
import ca.ulaval.glo2003.entity.Proprietaire;
import ca.ulaval.glo2003.entity.Restaurant;
import ca.ulaval.glo2003.repository.RestaurantRespository;
import ca.ulaval.glo2003.service.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

public class RestaurantResource {

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRestaurant(@HeaderParam("propriétaire") int noProprietaire, Restaurant newRestaurant) {

        if (!RestaurantRespository.proprietaireExiste(noProprietaire))
            return Response.status(Response.Status.BAD_REQUEST).entity("Le propriétaire spécifié n'existe pas.").build();


        Error valid = restaurantService.validerParametrePresent(newRestaurant);

        if (valid!=null)
            return Response.status(Response.Status.BAD_REQUEST).entity(valid).build();

        valid = restaurantService.validerParametreValide(newRestaurant);

        if (valid!=null)
            return Response.status(Response.Status.BAD_REQUEST).entity(valid).build();

        restaurantService.addRestaurant(noProprietaire,newRestaurant);

        return Response.created(URI.create("http://localhost:8080/api/restautant/" + newRestaurant.getNoRestaurant())).build();
    }

    private RestaurantService restaurantService;

    public RestaurantResource() {
    }
}
