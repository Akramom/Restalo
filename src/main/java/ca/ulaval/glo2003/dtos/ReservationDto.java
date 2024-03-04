package ca.ulaval.glo2003.dtos;

import ca.ulaval.glo2003.entity.Customer;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDto(
    String id,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    int durationInMin,
    int groupSize,
    Customer customer) {}
