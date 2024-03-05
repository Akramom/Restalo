package ca.ulaval.glo2003.domain.error;

public class ErrorBuilder {
  public ErrorBuilder() {}

  public Error notFoundError(String description) {
    return new Error(ErrorType.NOT_FOUND, description);
  }

  public Error invalidError(String description) {
    return new Error(ErrorType.INVALID_PARAMETER, description);
  }

  public Error missingError(String description) {
    return new Error(ErrorType.MISSING_PARAMETER, description);
  }

  public Object error(String description) {
    return new Error(ErrorType.ERROR, description);
  }
}
