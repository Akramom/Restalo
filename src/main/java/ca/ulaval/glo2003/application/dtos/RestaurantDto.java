package ca.ulaval.glo2003.application.dtos;

public record RestaurantDto(
    String id, String name, int capacity, HoursDto hours, ReservationDurationDto reservations) {}
