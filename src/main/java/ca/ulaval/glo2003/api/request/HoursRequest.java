package ca.ulaval.glo2003.api.request;

import ca.ulaval.glo2003.util.Constante;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class HoursRequest {
  @NotNull(message = Constante.MISSING_RESTAURANT_PARAMETER)
  private LocalTime open;

  @NotNull(message = Constante.MISSING_RESTAURANT_PARAMETER)
  private LocalTime close;

  public HoursRequest(LocalTime open, LocalTime close) {
    this.open = open;
    this.close = close;
  }

  public HoursRequest() {}

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
