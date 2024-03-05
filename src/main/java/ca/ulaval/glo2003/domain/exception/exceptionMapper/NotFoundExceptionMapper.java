package ca.ulaval.glo2003.domain.exception.exceptionMapper;

import ca.ulaval.glo2003.domain.error.ErrorBuilder;
import ca.ulaval.glo2003.domain.exception.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

  private ErrorBuilder errorBuilder;

  @Override
  public Response toResponse(NotFoundException exception) {
    this.errorBuilder = new ErrorBuilder();

    return Response.status(Response.Status.NOT_FOUND)
        .entity(errorBuilder.notFoundError(exception.getMessage()))
        .build();
  }
}
