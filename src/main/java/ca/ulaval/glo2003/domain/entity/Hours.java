package ca.ulaval.glo2003.domain.entity;

import dev.morphia.annotations.Entity;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Hours {
  private LocalTime open;
  private LocalTime close;

  public Hours(LocalTime open, LocalTime close) {
    this.open = open;
    this.close = close;
  }

  public Hours() {}

  public LocalTime getOpen() {
    return open;
  }

  public void setOpen(LocalTime open) {
    this.open = open;
  }

  public LocalTime getClose() {
    return close;
  }

  public void setClose(LocalTime close) {
    this.close = close;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Hours hours)) return false;
    return Objects.equals(open, hours.open) && Objects.equals(close, hours.close);
  }

  @Override
  public int hashCode() {
    return Objects.hash(open, close);
  }

  @Override
  public String toString() {
    return "Hours{" + "open=" + open + ", close=" + close + '}';
  }
}
