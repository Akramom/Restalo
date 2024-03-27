package ca.ulaval.glo2003.api.exceptionMapper;

import ca.ulaval.glo2003.domain.error.ErrorBuilder;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MissingParameterExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {
  private ErrorBuilder errorBuilder;

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    this.errorBuilder = new ErrorBuilder();

    String errorMessage = getFirstErrorMessage(exception);
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(errorBuilder.missingError(errorMessage))
        .build();
  }

  public String getFirstErrorMessage(ConstraintViolationException exception) {
    return exception.getConstraintViolations().iterator().next().getMessage();
  }
}
