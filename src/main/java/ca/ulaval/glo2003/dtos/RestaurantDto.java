package ca.ulaval.glo2003.dtos;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.ReservationDuration;

public record RestaurantDto(
    String id, String name, int capacity, Hours hours, ReservationDuration reservation) {}
