package ca.ulaval.glo2003.domain.entity;

import java.time.LocalTime;

public record Opened(LocalTime from, LocalTime to) {}
