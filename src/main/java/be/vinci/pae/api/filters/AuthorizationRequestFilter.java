package be.vinci.pae.api.filters;

import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.domain.interfaces.UserUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.time.Instant;
import java.util.Date;

/**
 * class used to verify JWT token before the annoted method is called. If the token is correct, then
 * the method is called, otherwise, the method sends an error.
 */
@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Inject
  private UserUCC userUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    String headerAnswer = requestContext.getHeaderString("Authorization");
    if (headerAnswer == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("A valid token is needed to access this resource").build());
    } else {
      DecodedJWT decodedToken = null;

      String token = headerAnswer.substring("Bearer ".length());
      try {
        decodedToken = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
            .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
      }

      Date now = Date.from(Instant.now());
      if (!decodedToken.getExpiresAt().after(now)) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            .entity("The token is expired").build());
      }

      int id = decodedToken.getClaim("id").asInt();

      UserDto user = userUCC.getOneUserById(id);

      requestContext.setProperty("user", user);
    }
  }
}
