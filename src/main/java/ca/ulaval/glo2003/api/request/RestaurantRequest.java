package ca.ulaval.glo2003.api.request;

import ca.ulaval.glo2003.application.dtos.HoursDto;
import ca.ulaval.glo2003.application.dtos.ReservationDurationDto;

public record RestaurantRequest(
    String id, String name, int capacity, HoursDto hours, ReservationDurationDto reservations) {}
