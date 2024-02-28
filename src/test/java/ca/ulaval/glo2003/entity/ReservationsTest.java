package ca.ulaval.glo2003.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationsTest {
  private static final LocalTime START_TIME = LocalTime.of(12, 12, 12);
  private static final LocalTime TO_NEXT_15_START_TIME = LocalTime.of(12, 15, 12);
  private static final int DURATION_IN_MIN = 120;

  Reservation reservation;

  @BeforeEach
  void setUp() {
    Customer customer = null;
    LocalDate date = LocalDate.of(2012, 12, 12);
    reservation = new Reservation(date, START_TIME, DURATION_IN_MIN, 3, customer);
  }

  @Test
  void constructorAutomaticallyCreatesValidReservationID() {
    assertNotNull(reservation.getId());
  }

  @Test
  void constructorAutomaticallyAdjustsStartTimeToNext15MinSlot() {
    assertEquals(TO_NEXT_15_START_TIME, reservation.getStartTime());
  }

  @Test
  void constructorAutomaticallyCalculatesEndTime() {
    LocalTime expectedEndTime = TO_NEXT_15_START_TIME.plusMinutes(DURATION_IN_MIN);
    assertEquals(expectedEndTime, reservation.getEndTime());
  }
}
