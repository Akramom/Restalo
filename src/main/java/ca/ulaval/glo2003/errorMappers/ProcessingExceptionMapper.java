package ca.ulaval.glo2003.errorMappers;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.core.Response;

public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException>{

    @Override
    public Response toResponse(ProcessingException exception){
        return Response.status(400).entity(
                "{\n\"error\": \"INVALID_PARAMETER\"," +
                        "\n\"description\": \"Invalid restaurant parameter. The restaurant could not be deserialize\"}"
        ).build();
    }
}
