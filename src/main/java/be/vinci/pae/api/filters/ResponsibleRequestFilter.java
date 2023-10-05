package be.vinci.pae.api.filters;

import be.vinci.pae.domain.interfaces.User.UserRole;
import be.vinci.pae.domain.interfaces.UserDto;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

/**
 * Class used to verify JWT token before the annoted method is called for responsible user. If the
 * token is correct, then the method is called, otherwise, the method sends an error.
 */
@Singleton
@Provider
@Responsible
public class ResponsibleRequestFilter extends AuthorizationRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    super.filter(requestContext);

    UserDto user = (UserDto) requestContext.getProperty("user");

    if (!user.getUserRole().equals(UserRole.RESPONSIBLE)) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity("User is not responsible : - " + user.getUserRole()).type("text/plain").build());
    }
  }

}
