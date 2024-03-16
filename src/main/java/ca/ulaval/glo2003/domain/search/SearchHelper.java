package ca.ulaval.glo2003.domain.search;

import ca.ulaval.glo2003.domain.entity.Opened;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.util.Util;
import java.util.List;
import java.util.stream.Collectors;

public class SearchHelper {
  public Boolean isSearchDtoNull(Search search) {
    if (search == null) return true;
    return search.getName() == null && search.getOpened() == null;
  }

  public Boolean isSearchDtoOpenedNull(Search search) {
    return search.getName() != null && search.getOpened() == null;
  }

  public Boolean isSearchDtoNameNull(Search search) {
    if (!isSearchDtoNull(search)) return search.getName() == null && search.getOpened() != null;
    else return false;
  }

  public List<Restaurant> searchByOpenedHour(List<Restaurant> allRestaurants, Opened opened) {
    if (opened.from() != null && opened.to() == null) {
      return allRestaurants.stream()
          .filter(restaurant -> restaurant.getHours().getClose().isAfter(opened.from()))
          .collect(Collectors.toList());
    }

    if (opened.from() == null && opened.to() != null) {
      return allRestaurants.stream()
          .filter(
              restaurant ->
                  restaurant.getHours().getClose().equals(opened.to())
                      || restaurant.getHours().getClose().isAfter(opened.to()))
          .collect(Collectors.toList());
    }

    return allRestaurants.stream()
        .filter(restaurant -> restaurant.getHours().getClose().isAfter(opened.from()))
        .filter(
            restaurant ->
                restaurant.getHours().getClose().equals(opened.to())
                    || restaurant.getHours().getClose().isAfter(opened.to()))
        .collect(Collectors.toList());
  }

  public List<Restaurant> searchByName(List<Restaurant> allRestaurants, String name) {
    return allRestaurants.stream()
        .filter(restaurant -> Util.containsSubstr_toLowerC(restaurant.getName(), name))
        .collect(Collectors.toList());
  }

  public List<Restaurant> search(List<Restaurant> allRestaurant, Search search) {

    if (search.getOpened().from() == null) {
      return allRestaurant.stream()
          .filter(
              restaurant -> Util.containsSubstr_toLowerC(restaurant.getName(), search.getName()))
          .filter(
              restaurant ->
                  restaurant.getHours().getClose().equals(search.getOpened().to())
                      || restaurant.getHours().getClose().isAfter(search.getOpened().to()))
          .collect(Collectors.toList());
    }

    if (search.getOpened().to() == null) {
      return allRestaurant.stream()
          .filter(
              restaurant -> Util.containsSubstr_toLowerC(restaurant.getName(), search.getName()))
          .filter(
              restaurant ->
                  restaurant.getHours().getClose().isAfter(search.getOpened().from())
                      && restaurant.getHours().getOpen().isBefore(search.getOpened().from()))
          .collect(Collectors.toList());
    }

    return allRestaurant.stream()
        .filter(restaurant -> Util.containsSubstr_toLowerC(restaurant.getName(), search.getName()))
        .filter(
            restaurant ->
                restaurant.getHours().getClose().isAfter(search.getOpened().from())
                    && restaurant.getHours().getOpen().isBefore(search.getOpened().from()))
        .filter(
            restaurant ->
                restaurant.getHours().getClose().equals(search.getOpened().to())
                    || restaurant.getHours().getClose().isAfter(search.getOpened().to()))
        .collect(Collectors.toList());
  }

  public List<Restaurant> searchRestaurant(List<Restaurant> allRestaurant, Search search) {
    if (isSearchDtoNull(search)) {
      return allRestaurant;
    }

    if (isSearchDtoOpenedNull(search)) {
      return searchByName(allRestaurant, search.getName());
    }

    if (isSearchDtoNameNull(search)) {
      return searchByOpenedHour(allRestaurant, search.getOpened());
    }

    return search(allRestaurant, search);
  }
}
