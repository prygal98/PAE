package be.vinci.pae.domain.interfaces;

/**
 * This interface describe the factory to create objects.
 */
public interface ObjectFactory {

  /**
   * get for new object.
   *
   * @return new object
   */
  ObjectDto getObject();
}
