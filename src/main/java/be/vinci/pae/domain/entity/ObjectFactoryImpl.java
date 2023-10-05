package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.ObjectFactory;

/**
 * This class is used to create Object objects.
 */
public class ObjectFactoryImpl implements ObjectFactory {

  @Override
  public ObjectDto getObject() {
    return new ObjectImpl();
  }

}
