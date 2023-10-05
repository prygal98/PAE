package be.vinci.pae.utils;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * This class is used to map exeptions.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();

    //API
    if (exception instanceof BadRequestException) {
      return Response.status(Status.BAD_REQUEST)
          .build();
    }

    // DOMAIN

    if (exception instanceof NotAuthorizedException) {
      return Response.status(Status.UNAUTHORIZED)
          .build();
    }
    if (exception instanceof NotAllowedException) {
      return Response.status(Status.BAD_REQUEST)
          .build();
    }

    // SERVICES
    if (exception instanceof NoSuchElementException) {
      return Response.status(Status.NOT_FOUND)
          .build();
    }

    if (exception instanceof SQLException) {
      return Response.status(Status.SERVICE_UNAVAILABLE)
          .build();
    }
    if (exception instanceof ClassNotFoundException) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .build();
    }
    if (exception instanceof RuntimeException) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .build();
    }

    // AUTRE
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .build();
  }
}