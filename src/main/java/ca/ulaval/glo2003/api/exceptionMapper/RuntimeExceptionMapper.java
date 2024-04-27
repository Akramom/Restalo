package ca.ulaval.glo2003.api.exceptionMapper;

import static ca.ulaval.glo2003.util.Constante.UNEXPECTED_ERROR;

import ca.ulaval.glo2003.domain.error.ErrorBuilder;
import io.sentry.Sentry;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException exception) {
    Sentry.captureException(exception);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(new ErrorBuilder().error(UNEXPECTED_ERROR))
        .build();
  }
}
