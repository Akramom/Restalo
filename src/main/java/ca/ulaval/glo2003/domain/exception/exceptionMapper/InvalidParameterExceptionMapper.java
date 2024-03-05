package ca.ulaval.glo2003.domain.exception.exceptionMapper;

import ca.ulaval.glo2003.domain.error.ErrorBuilder;
import ca.ulaval.glo2003.domain.exception.InvalidParameterException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class InvalidParameterExceptionMapper implements ExceptionMapper<InvalidParameterException> {

  private ErrorBuilder errorBuilder;

  @Override
  public Response toResponse(InvalidParameterException exception) {
    this.errorBuilder = new ErrorBuilder();

    return Response.status(Response.Status.BAD_REQUEST)
        .entity(errorBuilder.invalidError(exception.getMessage()))
        .build();
  }
}
