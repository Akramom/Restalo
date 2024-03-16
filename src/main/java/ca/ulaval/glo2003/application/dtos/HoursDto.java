package ca.ulaval.glo2003.application.dtos;

import java.time.LocalTime;

public class HoursDto {
  private LocalTime open;
  private LocalTime close;

  public HoursDto(LocalTime open, LocalTime close) {
    this.open = open;
    this.close = close;
  }

  public HoursDto() {}

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
}
