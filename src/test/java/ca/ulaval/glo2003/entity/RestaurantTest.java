package ca.ulaval.glo2003.entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private Restaurant restaurant;
    private Hours hours;
    private ReservationsRequest reservations;

    @BeforeEach
    void setUp() {
        hours = new Hours(LocalTime.NOON,LocalTime.MIDNIGHT);
        reservations = new ReservationsRequest(60);
        restaurant = new Restaurant("Poulet_Rouge", 100, hours, reservations);
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
        assertEquals(reservations, restaurant.getReservations());
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
        assertNull(defaultRestaurant.getReservations());
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
    void testSetId() {
        restaurant.setId("000");
        assertEquals("000", restaurant.getId());
    }

    @Test
    void testSetHours() {
        Hours newHours = new Hours(LocalTime.MIN,LocalTime.MAX);
        restaurant.setHours(newHours);
        assertEquals(newHours, restaurant.getHours());
    }

    @Test
    void testSetReservations() {
        ReservationsRequest newReservations = new ReservationsRequest(30);
        restaurant.setReservations(newReservations);
        assertEquals(newReservations, restaurant.getReservations());
    }
}
