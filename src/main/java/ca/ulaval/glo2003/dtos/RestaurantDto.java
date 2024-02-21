package ca.ulaval.glo2003.dtos;

import ca.ulaval.glo2003.entity.Hours;
import ca.ulaval.glo2003.entity.ReservationsRequest;

public record RestaurantDto(String id, String name, int capacity, Hours hours, ReservationsRequest reservation) {}
