package ca.ulaval.glo2003.errorMappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException exception) {
    return Response.status(500)
        .entity(
            "{\n\"error\": \"ERROR\","
                + "\n\"description\": \"An unexpected error occurred. Please review your request.\"}")
        .build();
  }
}
