package ca.ulaval.glo2003.util;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.entity.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ErrorBuilderTest {

  private final String INVALID_MESSAGE = "invalid error";
  private final String MISSING_MESSAGE = "missing error";
  private Error invalidError;
  private Error missingError;
  private ErrorBuilder errorBuilder;

  @BeforeEach
  void setUp() {
    errorBuilder = new ErrorBuilder();
  }

  @Test
  void testInvalidError() {
    missingError = errorBuilder.missingError(MISSING_MESSAGE);

    assertAll(
        "missing error",
        () -> assertThat(missingError).isNotNull(),
        () -> assertThat(missingError.getError()).isEqualTo(ErrorType.MISSING_PARAMETER),
        () -> assertThat(missingError.getDescription()).isEqualTo(MISSING_MESSAGE));
  }

  @Test
  void testMissingError() {
    invalidError = errorBuilder.invalidError(INVALID_MESSAGE);

    assertAll(
        "invalid error",
        () -> assertThat(invalidError).isNotNull(),
        () -> assertThat(invalidError.getError()).isEqualTo(ErrorType.INVALID_PARAMETER),
        () -> assertThat(invalidError.getDescription()).isEqualTo(INVALID_MESSAGE));
  }
}
