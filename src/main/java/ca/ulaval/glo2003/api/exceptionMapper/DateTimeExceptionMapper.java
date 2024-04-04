package ca.ulaval.glo2003.api.exceptionMapper;

import static ca.ulaval.glo2003.util.Constante.INVALID_DATE;

import ca.ulaval.glo2003.domain.error.ErrorBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.time.format.DateTimeParseException;

public class DateTimeExceptionMapper implements ExceptionMapper<DateTimeParseException> {

  private ErrorBuilder errorBuilder;

  @Override
  public Response toResponse(DateTimeParseException exception) {
    this.errorBuilder = new ErrorBuilder();

    return Response.status(Response.Status.BAD_REQUEST)
        .entity(errorBuilder.invalidError(INVALID_DATE + " : " + exception.getParsedString()))
        .build();
  }
}
