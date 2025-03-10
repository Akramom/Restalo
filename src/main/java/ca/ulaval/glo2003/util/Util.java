package ca.ulaval.glo2003.util;

import java.time.LocalTime;
import java.util.UUID;

public class Util {

  public static String generateId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  public static LocalTime ajustStartTimeToNext15Min(LocalTime startTime) {
    return addToNext15MinSlot(startTime);
  }

  public static LocalTime calculEndTime(LocalTime startTime, int durationInMin) {
    return startTime.plusMinutes(durationInMin);
  }

  public static LocalTime addToNext15MinSlot(LocalTime time) {
    int minutes = time.getMinute();
    int minutesOverThePrevious15MinSlot = minutes % Constante.AJUST_TIME;
    if (minutesOverThePrevious15MinSlot == 0) {
      return time;
    }
    return time.plusMinutes(Constante.AJUST_TIME - minutesOverThePrevious15MinSlot);
  }

  public static LocalTime adjustToPrevious15Minutes(LocalTime time) {
    int minutes = time.getMinute();
    int minutesOverThePrevious15MinSlot = minutes % Constante.AJUST_TIME;
    if (minutesOverThePrevious15MinSlot == 0) {
      return time;
    }
    return time.minusMinutes(minutesOverThePrevious15MinSlot);
  }

  public static boolean containsSubstr_toLowerC(String originalStr, String subStr) {
    return originalStr
        .replaceAll("\\s+", "")
        .toLowerCase()
        .contains(subStr.replaceAll("\\s+", "").toLowerCase());
  }
}
