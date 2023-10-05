package be.vinci.pae.domain.interfaces;

/**
 * This interface describe the factory to create a type objects.
 */
public interface TypeObjectFactory {

  /**
   * get for new type object.
   *
   * @return new type
   */
  TypeObjectDto getType();

}
