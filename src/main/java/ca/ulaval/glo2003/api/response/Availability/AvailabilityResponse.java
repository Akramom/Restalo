package ca.ulaval.glo2003.api.response.Availability;

import java.time.LocalDateTime;

public record AvailabilityResponse(LocalDateTime start, int remainingPlaces) {}
