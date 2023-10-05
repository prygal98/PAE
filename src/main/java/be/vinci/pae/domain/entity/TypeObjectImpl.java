package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.TypeObject;
import be.vinci.pae.domain.interfaces.TypeObjectDto;

/**
 * Implementation of the TypeObject and TypeObjectDto
 * interfaces that represents a type of object in the application.
 */
public class TypeObjectImpl implements TypeObject, TypeObjectDto {

  private int id;
  private String type;

  /**
   * Creates a new TypeObjectImpl object with default values for the fields.
   */
  public TypeObjectImpl() {

  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setObjectType(String type) {
    this.type = type;
  }
}


