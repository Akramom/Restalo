package ca.ulaval.glo2003.errorMappers;

import ca.ulaval.glo2003.entity.Error;
import ca.ulaval.glo2003.entity.ErrorType;
import ca.ulaval.glo2003.exception.MissingParameterException;
import ca.ulaval.glo2003.util.ErrorBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MissingParameterExceptionMapper implements ExceptionMapper<MissingParameterException> {
  private ErrorBuilder errorBuilder;

  @Override
  public Response toResponse(MissingParameterException exception) {
    this.errorBuilder = new ErrorBuilder();
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(new Error(ErrorType.MISSING_PARAMETER, exception.getMessage()))
        .build();
  }
}
