package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class provides methods to read configuration properties from a file.
 */
public class Config {

  private static Properties props;

  /**
   * Load the configuration properties from a file.
   *
   * @param file the name of the configuration file.
   * @throws WebApplicationException if an error occurs while reading the configuration file.
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain")
              .build());
    }
  }

  /**
   * get a value from a config file (which needs to be loaded beforehand).
   *
   * @param key the key attached to a value.
   * @return the value attached to this key.
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

}
