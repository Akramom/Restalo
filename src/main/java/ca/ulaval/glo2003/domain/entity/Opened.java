package ca.ulaval.glo2003.domain.entity;

import dev.morphia.annotations.Entity;
import java.time.LocalTime;

@Entity
public record Opened(LocalTime from, LocalTime to) {}
