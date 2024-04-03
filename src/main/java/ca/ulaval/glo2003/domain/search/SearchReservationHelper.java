package ca.ulaval.glo2003.domain.search;

import ca.ulaval.glo2003.domain.entity.Reservation;
import ca.ulaval.glo2003.util.Util;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class SearchReservationHelper {
  BiPredicate<Reservation, String> customNameFilter =
      (reservation, name) ->
          Util.containsSubstr_toLowerC(reservation.getCustomer().getName(), name);
  BiPredicate<Reservation, LocalDate> dateFilter =
      (reservation, date) -> reservation.getDate().equals(date);

  public SearchReservationHelper() {}

  public List<Reservation> searchByDate(List<Reservation> allReservation, LocalDate date) {

    return allReservation.stream()
        .filter(reservation -> dateFilter.test(reservation, date))
        .toList();
  }

  public List<Reservation> searchByCustomName(List<Reservation> allReservation, String customName) {
    return allReservation.stream()
        .filter(reservation -> customNameFilter.test(reservation, customName))
        .collect(Collectors.toList());
  }

  public List<Reservation> search(
      List<Reservation> allReservation, LocalDate date, String customName) {

    return allReservation.stream()
        .filter(reservation -> dateFilter.test(reservation, date))
        .filter(reservation -> customNameFilter.test(reservation, customName))
        .collect(Collectors.toList());
  }

  public List<Reservation> searchReservation(
      List<Reservation> allReservations, String date, String customName) {
    LocalDate parsedDate = isNullOrEmpty(date) ? null : LocalDate.parse(date);
    if (parsedDate == null && isNullOrEmpty(customName)) return allReservations;
    if (parsedDate == null) return searchByCustomName(allReservations, customName);
    if (isNullOrEmpty(customName)) return searchByDate(allReservations, parsedDate);

    return search(allReservations, parsedDate, customName);
  }

  private boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }
}
