package ca.ulaval.glo2003.application.dtos;

public record UpdateRestaurantDto(String name, int capacity, HoursDto hours,ReservationDurationDto reservations) {}
