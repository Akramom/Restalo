package ca.ulaval.glo2003.api.exceptionMapper;

import static ca.ulaval.glo2003.util.Constante.ERROR_DESERIALIZE;

import ca.ulaval.glo2003.domain.error.ErrorBuilder;
import io.sentry.Sentry;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {

  @Override
  public Response toResponse(ProcessingException exception) {
    Sentry.captureException(exception);
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(new ErrorBuilder().invalidError(ERROR_DESERIALIZE))
        .build();
  }
}
