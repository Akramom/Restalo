package ca.ulaval.glo2003.errorMappers;

import ca.ulaval.glo2003.exception.InvalidParameterException;
import ca.ulaval.glo2003.util.ErrorBuilder;
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
