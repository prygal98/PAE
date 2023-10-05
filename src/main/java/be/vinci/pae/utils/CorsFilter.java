package be.vinci.pae.utils;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * This class provides the Cors filter.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

  /**
   * List of the config filter class for the backend use requests.
   */
  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext)
      throws IOException {
    MultivaluedMap<String, Object> headers = responseContext.getHeaders();

    headers.add("Access-Control-Allow-Origin", "*");
    headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    headers.add(
        "Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization"
    );
  }
}
