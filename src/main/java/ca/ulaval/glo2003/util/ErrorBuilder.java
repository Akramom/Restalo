package ca.ulaval.glo2003.util;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.entity.ErrorType;

public class ErrorBuilder {
  public ErrorBuilder() {}

  public Error invalidError(String description) {
    return new Error(ErrorType.INVALID_PARAMETER, description);
  }

  public Error missingError(String description) {
    return new Error(ErrorType.MISSING_PARAMETER, description);
  }
}
