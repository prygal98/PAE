package be.vinci.pae.domain.interfaces;


/**
 * This interface describe the data of the Objects types.
 */
public interface TypeObjectDto {

  /**
   * get the id.
   *
   * @return the id.
   */
  int getId();

  /**
   * set the id.
   *
   * @param id the id to be set
   */
  void setId(int id);

  /**
   * get the type.
   *
   * @return the type object.
   */
  String getType();

  /**
   * set the type.
   *
   * @param type the date
   */
  void setObjectType(String type);

}
