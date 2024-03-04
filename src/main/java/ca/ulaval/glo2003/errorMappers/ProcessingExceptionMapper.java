package ca.ulaval.glo2003.errorMappers;

import static ca.ulaval.glo2003.util.Constante.ERROR_DESERIALIZE;

import ca.ulaval.glo2003.util.ErrorBuilder;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {

  @Override
  public Response toResponse(ProcessingException exception) {
    return Response.status(400).entity(new ErrorBuilder().invalidError(ERROR_DESERIALIZE)).build();
  }
}
