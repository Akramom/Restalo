package ca.ulaval.glo2003.util;

import ca.ulaval.glo2003.domain.entity.Opened;
import ca.ulaval.glo2003.domain.entity.Restaurant;
import ca.ulaval.glo2003.domain.entity.Search;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Util {

  public static String generateId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  public static Boolean isSearchDtoNull(Search search) {
    if (search == null) return true;
    return search.getName() == null && search.getOpened() == null;
  }

  public static Boolean isSearchDtoOpenedNull(Search search) {
    return search.getName() != null && search.getOpened() == null;
  }

  public static Boolean isSearchDtoNameNull(Search search) {
    if (!isSearchDtoNull(search)) return search.getName() == null && search.getOpened() != null;
    else return false;
  }

  public static LocalTime ajustStartTimeToNext15Min(LocalTime startTime) {
    return addToNext15MinSlot(startTime);
  }

  public static LocalTime addToNext15MinSlot(LocalTime time) {
    int minutes = time.getMinute();
    int minutesOverThePrevious15MinSlot = minutes % 15;
    if (minutesOverThePrevious15MinSlot == 0) {
      return time;
    }
    return time.plusMinutes(15 - minutesOverThePrevious15MinSlot);
  }

  public static LocalTime setEndTime(LocalTime startTime, int durationInMin) {
    return startTime.plusMinutes(durationInMin);
  }

  public static List<Restaurant> searchByOpenedHour(
      List<Restaurant> allRestaurants, Opened opened) {
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

  public static List<Restaurant> searchByName(List<Restaurant> allRestaurants, String name) {
    return allRestaurants.stream()
        .filter(restaurant -> containsSubstr_toLowerC(restaurant.getName(), name))
        .collect(Collectors.toList());
  }

  public static List<Restaurant> search(List<Restaurant> allRestaurant, Search search) {
    checkAndFixNullFromOrTo(search);

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

  private static void checkAndFixNullFromOrTo(Search search) {
    if (search.getOpened().to() == null) {
      LocalTime newTo = search.getOpened().from();
      Opened newOpened = new Opened(search.getOpened().from(), newTo);
      search.setOpened(newOpened);
    }

    if (search.getOpened().from() == null) {
      LocalTime newFrom = search.getOpened().to();
      Opened newOpened = new Opened(newFrom, search.getOpened().to());
      search.setOpened(newOpened);
    }
  }

  public static boolean containsSubstr_toLowerC(String originalStr, String subStr) {
    return originalStr
        .replaceAll("\\s+", "")
        .toLowerCase()
        .contains(subStr.replaceAll("\\s+", "").toLowerCase());
  }
}
