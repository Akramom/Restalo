package ca.ulaval.glo2003.domain;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.domain.entity.*;
import ca.ulaval.glo2003.domain.search.SearchReservationHelper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchReservationHelperTest {
  private SearchReservationHelper searchReservationHelper;
  private List<Reservation> reservations;
  private final String NAME = "Jonh Doe";
  private final String EMAIL = "jj@jj.com";
  private final String PHONE = "4184444444";
  private final String OTHER_NAME = "Alice";
  private final String OTHER_EMAIL = "aa@aa.com";
  private final String OTHER_PHONE = "41855555555";
  private final int GROUP_SIZE = 2;
  private final int OTHER_GROUP_SIZE = 4;
  private final String ID = "1001";
  private final String OTHER_ID = "2001";
  private final LocalTime START_TIME = LocalTime.NOON;
  private final LocalTime END_TIME = LocalTime.MIDNIGHT;
  private final LocalTime OTHER_START_TIME = LocalTime.of(2, 2, 2);
  private final LocalTime OTHER_END_TIME = LocalTime.of(3, 3, 3);
  private LocalDate DATE = LocalDate.of(2002, 2, 20);
  private LocalDate OTHER_DATE = LocalDate.of(3003, 3, 30);
  private Reservation reservation;
  private Reservation otherReservation;
  private Customer customer;
  private Customer otherCustomer;

  @BeforeEach
  void setUp() {
    searchReservationHelper = new SearchReservationHelper();
    reservations = new ArrayList<>();
    customer = new Customer(NAME, EMAIL, PHONE);
    otherCustomer = new Customer(OTHER_NAME, OTHER_EMAIL, OTHER_PHONE);
    reservation = new Reservation(ID, DATE, START_TIME, END_TIME, GROUP_SIZE, customer);
    otherReservation =
        new Reservation(
            OTHER_ID,
            OTHER_DATE,
            OTHER_START_TIME,
            OTHER_END_TIME,
            OTHER_GROUP_SIZE,
            otherCustomer);
    reservations.add(reservation);
    reservations.add(otherReservation);
  }

  @Test
  void givenValidDate_whenSearchByDate_thenReturnsReservationWithSameDate() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);

    List<Reservation> returnedList = searchReservationHelper.searchByDate(reservations, DATE);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenCustomerName_whenSearchByCustomName_thenReturnsCustomerReservations() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);

    List<Reservation> returnedList = searchReservationHelper.searchByCustomName(reservations, NAME);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenCustomerNameAndDate_whenSearch_thenReturnsCustomerReservationsAtDate() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);

    List<Reservation> returnedList = searchReservationHelper.search(reservations, DATE, NAME);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void
      givenCustomerNameAndDateAsString_whenSearchReservation_thenReturnsCustomerReservationsAtDate() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);
    String dateString = DATE.toString();

    List<Reservation> returnedList =
        searchReservationHelper.searchReservation(reservations, dateString, NAME);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenCustomerNameAndNoDate_whenSearchReservation_thenReturnsCustomerReservations() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);

    List<Reservation> returnedList =
        searchReservationHelper.searchReservation(reservations, null, NAME);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenDateAsStringAndNoCustomerName_whenSearchReservation_thenReturnsReservationsAtDate() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);
    String dateString = DATE.toString();

    List<Reservation> returnedList =
        searchReservationHelper.searchReservation(reservations, dateString, null);

    assertEquals(expectedList, returnedList);
  }

  @Test
  void givenNoCustomerNameOrDate_whenSearchReservation_thenReturnsAllReservations() {
    List<Reservation> expectedList = new ArrayList<>();
    expectedList.add(reservation);
    expectedList.add(otherReservation);

    List<Reservation> returnedList =
        searchReservationHelper.searchReservation(reservations, null, null);

    assertEquals(expectedList, returnedList);
  }
}
