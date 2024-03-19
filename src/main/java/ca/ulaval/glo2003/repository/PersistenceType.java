package ca.ulaval.glo2003.repository;

public enum PersistenceType {
  MONGO("mongo"),
  INMEMORY("inmemory");

  private final String value;

  PersistenceType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static PersistenceType fromString(String text) {
    for (PersistenceType type : PersistenceType.values()) {
      if (type.getValue().equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("No persistence type with value " + text + " found");
  }
}
