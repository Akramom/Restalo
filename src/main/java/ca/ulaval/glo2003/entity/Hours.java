package ca.ulaval.glo2003.entity;

import java.time.LocalTime;

public class Hours {

  private LocalTime open;
  private LocalTime close;
  private int reservationDuration;

  public Hours(LocalTime open, LocalTime close, int reservationDuration) {
    this.open = open;
    this.close = close;
    this.reservationDuration = reservationDuration;
  }

  public Hours() {}

  public int getReservationDuration() {
    return reservationDuration;
  }

  public Hours(LocalTime open, LocalTime close) {
    this.open = open;
    this.close = close;
  }

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
  public String toString() {
    return "Hours{" + "open=" + open + ", close=" + close + '}';
  }
}
