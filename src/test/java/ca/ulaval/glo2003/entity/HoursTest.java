package ca.ulaval.glo2003.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HoursTest {

  public Hours heure1;
  public Hours heure2;
  public LocalTime open;
  public LocalTime close;

  @BeforeEach
  void setUp() {
    heure1 = new Hours();
    open = LocalTime.now();
    close = open.plusHours(10);
    heure2 = new Hours(open, close);
  }

  @Test
  void getOpen() {
    assertEquals(open, heure2.getOpen());
    assertEquals(null, heure1.getOpen());
  }

  @Test
  void setOpen() {
    heure1.setOpen(open);
    assertEquals(open, heure1.getOpen());
  }

  @Test
  void getClose() {
    assertEquals(close, heure2.getClose());
    assertEquals(null, heure1.getClose());
  }

  @Test
  void setClose() {
    heure1.setClose(close);
    assertEquals(close, heure1.getClose());
  }

  @Test
  void toStringTest() {

    open = LocalTime.of(10, 30, 45);
    close = LocalTime.of(19, 30, 45);
    heure1.setOpen(open);
    heure1.setClose(close);

    assertEquals("Hours{open=10:30:45, close=19:30:45}", heure1.toString());
  }
}
