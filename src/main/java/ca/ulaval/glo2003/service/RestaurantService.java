package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.repository.*;
import ca.ulaval.glo2003.entity.*;
import jakarta.ws.rs.core.Response;

import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class RestaurantService {

    private RestaurantRespository restaurantRepository;

    public RestaurantService() {
    }

    public Response verifyCreateRestaurantReq(String ID, String restaurantInfos){
        if(!isExistingOwnerID(ID)){
            return invalidParameterResponse("Invalid owner ID.");
        }

        List<List<String>> infos = parseRestaurantInfos(restaurantInfos);
        if(!validName(infos.get(0))){
            return missingParameterResponse("Missing restaurant name.");
        }
        if(!validCapacity(infos.get(1))){
            if(infos.get(1).get(0) == ""){
                return missingParameterResponse("Missing restaurant capacity.");
            }
            else{
                return invalidParameterResponse("Restaurant capacity must be greater than 0.");
            }
        }

        List<Integer> openHour;
        List<Integer> closeHour;
        try{
            openHour = hourStrToInt(infos.get(2).get(0));
            closeHour = hourStrToInt(infos.get(2).get(1));
        } catch(Exception e){
            return invalidParameterResponse("Hours can only contain numbers.");
        }

        if(!validOpeningHours(openHour, closeHour)){
            return invalidOpeningHoursResponse(openHour, closeHour);
        }
        
        Restaurant restaurant = createRestaurant(infos, openHour, closeHour);
        addRestaurantRepository(ID, restaurant);

        return validCreateRestaurantResponse(restaurant);
    }
    
    private void addRestaurantRepository(String ID, Restaurant restaurant){
        restaurantRepository.addRestaurant(Integer.parseInt(ID), restaurant);
    }
    private Restaurant createRestaurant(List<List<String>> infos, List<Integer> openHour, List<Integer> closeHour){
        String name = infos.get(0).get(0);
        int capacity = Integer.parseInt(infos.get(1).get(0));
        Hours hourObj = createHourObj(openHour,closeHour);
        return new Restaurant(name,  capacity, hourObj);
    }

    private Hours createHourObj(List<Integer> openHour, List<Integer> closeHour){
        LocalTime open = LocalTime.of(openHour.get(0), openHour.get(1), openHour.get(2));
        LocalTime close = LocalTime.of(closeHour.get(0), closeHour.get(1), closeHour.get(2));
        return new Hours(open, close);
    }

    private Boolean validName(List<String> parsedName){
        if (checkListEmpty(parsedName)){
            return false;
        }
        return true;
    }

    private Boolean validCapacity(List<String> parsedCapacity){
        if (checkListEmpty(parsedCapacity)){
            return false;
        }

        int capacity;
        try{
            capacity = Integer.parseInt(parsedCapacity.getFirst());
        } catch (Exception e){
            return false;
        }
        if(capacity <= 0){
            return false;
        }
        return true;
    }

    private Boolean validOpeningHours(List<Integer> openHour, List<Integer> closeHour){
        if (!validTime(openHour) || !validTime(closeHour)){
            return false;
        }

        int timeOpen = closeHour.get(0) - openHour.get(0);
        if (timeOpen < 1){
            return false;
        }

        return true;
    }

    private Boolean validTime(List<Integer> time){
        int hour = time.get(0);
        int minutes = time.get(1);
        int seconds = time.get(2);

        if(hour > 23){
            return false;
        }
        if(minutes > 59 || seconds > 59){
            return false;
        }
        return true;
    }

    private Boolean isExistingOwnerID(String ID){
        for (Proprietaire owner: restaurantRepository.getProprietaires()){
            String ownerID = Integer.toString(owner.getNoProprietaire());
            if (ID.equals(ownerID)){
                return true;
            }
        }
        return false;
    }

    private Boolean checkListEmpty(List<String> list){
        for (String string: list){
            if (!string.isEmpty()){
                return false;
            }
        }
        return true;
    }

    private Response validCreateRestaurantResponse(Restaurant restaurant){
        int restaurantIDInt = restaurant.getNoRestaurant();
        String restaurantID = Integer.toString(restaurantIDInt);
        String header = "http://localhost:8080/restaurants/" + restaurantID;
        return Response.status(201).header("URL", header).build();
    }

    private Response invalidOpeningHoursResponse(List<Integer> openHour, List<Integer> closeHour){
        if(!validTime(openHour)){
            return invalidParameterResponse("Invalid opening hour.");
        }
        if(!validTime(closeHour)){
            return invalidParameterResponse("Invalid closing hour.");
        }
        return invalidParameterResponse("The restaurant as to be open for at least an hour.");
    }

    private Response invalidParameterResponse(String reason){
        String body = """      
        {
            error: "INVALID_PARAMETER",
            description: " """ + reason + """ 
        "
        }
        """;
        return Response.status(400).entity(body).build();
    }

    private Response missingParameterResponse(String reason){
        String body = """
        {
            error: "MISSING_PARAMETER",
                    description: " """ + reason + """
        "
        }
        """;
        return Response.status(400).entity(body).build();
    }

    private List<Integer> hourStrToInt(String time) throws Exception{
        List<Integer> hourInInt = new ArrayList<Integer>();
        int hour;
        int minute;
        int second;

        try{
            String hourStr = time.substring(0,2);
            hour = Integer.parseInt(hourStr);
            hourInInt.add(hour);

            String minuteStr = time.substring(3,5);
            minute = Integer.parseInt(minuteStr);
            hourInInt.add(minute);

            String secondStr = time.substring(6,8);
            second = Integer.parseInt(secondStr);
            hourInInt.add(second);
        } catch(Exception e){
            throw new Exception("Time cannot contain letters or special characters");
        }

        return hourInInt;
    }

    private List<List<String>> parseRestaurantInfos(String restaurantInfos){
        List<List<String>> parsedInfos = new ArrayList<List<String>>();

        return parsedInfos;
    }
}
