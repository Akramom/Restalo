package ca.ulaval.glo2003.errorMappers;

import ca.ulaval.glo2003.util.ErrorBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  public static final String UNEXPECTED_ERROR =
      "An unexpected error occurred. Please review your request.";

  @Override
  public Response toResponse(RuntimeException exception) {
    return Response.status(500).entity(new ErrorBuilder().error(UNEXPECTED_ERROR)).build();
  }
}
