package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.TypeObjectDto;
import be.vinci.pae.domain.interfaces.TypeObjectFactory;

/**
 * Implementation of the TypeObjectFactory interface that provides a method for creating new
 * TypeObjectDto objects.
 */
public class TypeObjectFactoryImpl implements TypeObjectFactory {

  @Override
  public TypeObjectDto getType() {
    return new TypeObjectImpl();
  }
}

