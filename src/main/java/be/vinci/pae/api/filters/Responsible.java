package be.vinci.pae.api.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * verify if the user is Responsible.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Responsible {
}
