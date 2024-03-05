package ca.ulaval.glo2003.application.dtos;

import ca.ulaval.glo2003.domain.entity.Hours;
import ca.ulaval.glo2003.domain.entity.ReservationDuration;

public record RestaurantDto(
    String id, String name, int capacity, Hours hours, ReservationDuration reservations) {}
