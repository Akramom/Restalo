package ca.ulaval.glo2003.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantTest {

  private Restaurant restaurant;
  private Hours hours;
  private ReservationDuration reservationDuration;

  @BeforeEach
  void setUp() {
    hours = new Hours(LocalTime.NOON, LocalTime.MIDNIGHT);
    reservationDuration = new ReservationDuration(70);
    restaurant = new Restaurant("Poulet_Rouge", 100, hours, reservationDuration);
  }

  @Test
  void testGetName() {
    assertEquals("Poulet_Rouge", restaurant.getName());
  }

  @Test
  void testGetCapacity() {
    assertEquals(100, restaurant.getCapacity());
  }

  @Test
  void testGetId() {
    assertNotNull(restaurant.getId());
  }

  @Test
  void testGetHours() {
    assertEquals(hours, restaurant.getHours());
  }

  @Test
  void testGetReservations() {
    assertEquals(reservationDuration, restaurant.getReservation());
  }

  @Test
  void testGenerateId() {
    String initialId = restaurant.getId();
    restaurant.generateId();
    String newId = restaurant.getId();
    assertNotNull(newId);
    assertNotEquals(initialId, newId);
    assertEquals(8, newId.length());
  }

  @Test
  void testDefaultConstructor() {
    Restaurant defaultRestaurant = new Restaurant();
    assertNull(defaultRestaurant.getName());
    assertEquals(0, defaultRestaurant.getCapacity());
    assertNull(defaultRestaurant.getHours());
    assertNull(defaultRestaurant.getReservation());
    assertNull(defaultRestaurant.getId());
  }

  @Test
  void testSetName() {
    restaurant.setName("Paris_Tacos");
    assertEquals("Paris_Tacos", restaurant.getName());
  }

  @Test
  void testSetCapacity() {
    restaurant.setCapacity(200);
    assertEquals(200, restaurant.getCapacity());
  }

  @Test
  void testSetHours() {
    Hours newHours = new Hours(LocalTime.MIN, LocalTime.MAX);
    restaurant.setHours(newHours);
    assertEquals(newHours, restaurant.getHours());
  }

  @Test
  void testSetReservationDuration() {
    restaurant.setReservations(reservationDuration);
    assertEquals(reservationDuration, restaurant.getReservation());
  }
}
