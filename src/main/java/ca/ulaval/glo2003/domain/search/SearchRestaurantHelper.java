package ca.ulaval.glo2003.domain.search;

import ca.ulaval.glo2003.domain.entity.Opened;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.util.Util;
import java.time.LocalTime;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class SearchRestaurantHelper {

  BiPredicate<Restaurant, String> nameFilter =
      (restaurant, name) -> Util.containsSubstr_toLowerC(restaurant.getName(), name);
  BiPredicate<Restaurant, LocalTime> openedFromFilter =
      (restaurant, from) ->
          restaurant.getHours().getClose().isAfter(from)
              && (restaurant.getHours().getOpen().isBefore(from)
                  || restaurant.getHours().getOpen().equals(from));
  BiPredicate<Restaurant, LocalTime> openedToFilter =
      (restaurant, to) ->
          restaurant.getHours().getClose().equals(to)
              || restaurant.getHours().getClose().isAfter(to);

  public SearchRestaurantHelper() {}

  public Boolean isSearchDtoOpenedNull(Search search) {
    return search.getOpened() == null;
  }

  public Boolean isSearchDtoNull(Search search) {
    return (search == null) || (search.getName() == null && search.getOpened() == null);
  }

  public Boolean isSearchDtoNameNull(Search search) {
    return search.getName() == null;
  }

  public List<Restaurant> searchByOpenedHour(List<Restaurant> allRestaurants, Opened opened) {
    if (opened.from() != null && opened.to() == null) {
      return allRestaurants.stream()
          .filter(restaurant -> openedFromFilter.test(restaurant, opened.from()))
          .collect(Collectors.toList());
    }

    if (opened.from() == null && opened.to() != null) {
      return allRestaurants.stream()
          .filter(restaurant -> openedToFilter.test(restaurant, opened.to()))
          .collect(Collectors.toList());
    }

    return allRestaurants.stream()
        .filter(restaurant -> openedFromFilter.test(restaurant, opened.from()))
        .filter(restaurant -> openedToFilter.test(restaurant, opened.to()))
        .collect(Collectors.toList());
  }

  public List<Restaurant> searchByName(List<Restaurant> allRestaurants, String name) {
    return allRestaurants.stream()
        .filter(restaurant -> nameFilter.test(restaurant, name))
        .collect(Collectors.toList());
  }

  public List<Restaurant> search(List<Restaurant> allRestaurant, Search search) {

    if (search.getOpened().from() == null) {
      return allRestaurant.stream()
          .filter(restaurant -> nameFilter.test(restaurant, search.getName()))
          .filter(restaurant -> openedToFilter.test(restaurant, search.getOpened().to()))
          .collect(Collectors.toList());
    }

    if (search.getOpened().to() == null) {
      return allRestaurant.stream()
          .filter(restaurant -> nameFilter.test(restaurant, search.getName()))
          .filter(restaurant -> openedFromFilter.test(restaurant, search.getOpened().from()))
          .collect(Collectors.toList());
    }

    return allRestaurant.stream()
        .filter(restaurant -> nameFilter.test(restaurant, search.getName()))
        .filter(restaurant -> openedFromFilter.test(restaurant, search.getOpened().from()))
        .filter(restaurant -> openedToFilter.test(restaurant, search.getOpened().to()))
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
