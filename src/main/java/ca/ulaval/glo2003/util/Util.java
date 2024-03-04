package ca.ulaval.glo2003.util;

import java.util.UUID;

public class Util {

  public static String generateId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }
}
