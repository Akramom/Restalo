package ca.ulaval.glo2003.domain.error;

public class Error {
  private ErrorType error;
  private String description;

  public Error() {}

  public Error(ErrorType error, String description) {
    this.error = error;
    this.description = description;
  }

  public ErrorType getError() {
    return error;
  }

  public void setError(ErrorType error) {
    this.error = error;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Error{" + "error=" + error + ", description='" + description + '\'' + '}';
  }
}
