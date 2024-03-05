package ca.ulaval.glo2003.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationsTest {
  private static final LocalTime START_TIME = LocalTime.of(12, 12, 12);
  private static final LocalTime TO_NEXT_15_START_TIME = LocalTime.of(12, 15, 12);
  private LocalDate date;
  private static final int DURATION_IN_MIN = 120;

  private Customer customer;

  private Reservation reservation;

  @BeforeEach
  void setUp() {
    customer = null;
    date = LocalDate.of(2012, 12, 12);
    reservation = new Reservation(date, START_TIME, DURATION_IN_MIN, 3, customer);
  }

  @Test
  void constructorAutomaticallyCreatesValidReservationID() {
    assertNotNull(reservation.getNumber());
  }

  @Test
  void constructorAutomaticallyAdjustsStartTimeToNext15MinSlot() {
    assertEquals(TO_NEXT_15_START_TIME, reservation.getStartTime());
  }

  @Test
  void constructorAutomaticallyCalculatesEndTime() {
    LocalTime expectedEndTime = reservation.getStartTime().plusMinutes(DURATION_IN_MIN);
    assertEquals(expectedEndTime, reservation.getEndTime());
  }
}
