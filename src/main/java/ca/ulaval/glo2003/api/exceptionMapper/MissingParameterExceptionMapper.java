package ca.ulaval.glo2003.api.exceptionMapper;

import ca.ulaval.glo2003.domain.error.ErrorBuilder;
import ca.ulaval.glo2003.domain.exception.MissingParameterException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MissingParameterExceptionMapper implements ExceptionMapper<MissingParameterException> {
  private ErrorBuilder errorBuilder;

  @Override
  public Response toResponse(MissingParameterException exception) {
    this.errorBuilder = new ErrorBuilder();
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(errorBuilder.missingError(exception.getMessage()))
        .build();
  }
}
