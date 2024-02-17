package ca.ulaval.glo2003.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ErrorTest {
  public Error invalid_error;
  public Error missing_error;
  public Error error;

  @BeforeEach
  void setUp() {
    missing_error = new Error(ErrorType.MISSING_PARAMETER, "missing parameter");
    invalid_error = new Error(ErrorType.INVALID_PARAMETER, "invalid parameter");
    error = new Error();
  }

  @Test
  void getError() {
    ErrorType errorType = missing_error.getError();
    assertEquals(ErrorType.MISSING_PARAMETER, errorType);
    assertEquals(null, error.getError());
  }

  @Test
  void setError() {
    error.setError(ErrorType.MISSING_PARAMETER);
    assertEquals(ErrorType.MISSING_PARAMETER, error.getError());
  }

  @Test
  void getDescription() {
    String description = missing_error.getDescription();

    assertEquals("missing parameter", description);
    assertEquals(null, error.getDescription());
  }

  @Test
  void setDescription() {
    error.setDescription("error");
    System.out.println(error);

    assertEquals("error", error.getDescription());
  }

  @Test
  void toStringTest() {
    error.setDescription("error");
    error.setError(ErrorType.MISSING_PARAMETER);

    String toString = error.toString();

    assertEquals("Error{error=MISSING_PARAMETER, description='error'}", toString);
  }
}
