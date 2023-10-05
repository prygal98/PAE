package be.vinci.pae.api.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * verify the headers and its token. Then, set the 'id' property to be the id taken from the token.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponsibleOrHelper {
}
