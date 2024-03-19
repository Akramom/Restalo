package ca.ulaval.glo2003.domain.entity;

import dev.morphia.annotations.Entity;
import java.time.LocalTime;

@Entity
public class Time {

  private LocalTime start;
  private LocalTime end;

  public Time(LocalTime open, LocalTime close) {
    this.start = open;
    this.end = close;
  }

  public Time() {}

  public LocalTime getStart() {
    return start;
  }

  public void setStart(LocalTime start) {
    this.start = start;
  }

  public LocalTime getEnd() {
    return end;
  }

  public void setEnd(LocalTime end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "Time{" + "start=" + start + ", end=" + end + '}';
  }
}
