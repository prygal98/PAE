package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.TypeObjectDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This interface describe the data access object for Objects.
 */
public interface ObjectDAO {

  /**
   * Retrieves a list of all objects in the database.
   *
   * @return a list of ObjectDto representing all objects in the database.
   */
  List<ObjectDto> getAllObjects();

  /**
   * Retrieves a list of all objects the objects types in the database.
   *
   * @return a list of TypesObjectDto representing all objects in the database.
   */
  List<TypeObjectDto> getAllTypesObject();

  /**
   * Retrieves the number of objects sold.
   *
   * @return the number of objects sold.
   */
  int getNumberOfSoldObjects();

  /**
   * Returns the number of proposed objects by year.
   *
   * @return a map : Year, number of objects
   */
  Map<Integer, Integer> getNumberOfProposedObjectsByPeriod();

  /**
   * Retrieves an object from the database by its unique identifier.
   *
   * @param id the unique identifier of the object to retrieve.
   * @return an ObjectDto representing the object with the given identifier.
   */
  ObjectDto getOneObjectById(int id);

  /**
   * Updates the state of an object in the database and returns the updated object.
   *
   * @param object the object as should be modified.
   * @return an ObjectDto representing the updated object.
   */
  ObjectDto updateOneObject(ObjectDto object);

  /**
   * Create a list of ObjectDto using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return a list of ObjectDto.
   * @throws SQLException an exception from the resultSet.
   */
  List<ObjectDto> makeObjects(ResultSet rs) throws SQLException;

  /**
   * Create a single ObjectDto using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return one ObjectDto.
   * @throws SQLException an exception from the resultSet.
   */
  ObjectDto makeOneObject(ResultSet rs) throws SQLException;

  /**
   * Create a list of objects type using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return a list of objectsTypeDTO.
   * @throws SQLException an exception from the resultSet.
   */
  List<TypeObjectDto> makeTypesObjects(ResultSet rs) throws SQLException;

  /**
   * Create a single type using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return one type.
   * @throws SQLException an exception from the resultSet.
   */
  TypeObjectDto makeOneTypeObject(ResultSet rs) throws SQLException;

  /**
   * sort with prices.
   *
   * @param priceMin pricemin to sort
   * @param priceMax pricemax to sort
   * @return the sorted list
   */
  List<ObjectDto> getObjectsBetweenTwoPrice(int priceMin, int priceMax);

  /**
   * Retrieves a list of all objects in the database from the user with this id.
   *
   * @param id the id of the user from whom we get the objects
   * @return a list of ObjectsDto
   */
  List<ObjectDto> getAllObjectsFromOneUser(int id);

  /**
   * Retrieves a list of all objects in the database with the recovery date.
   *
   * @param date the date to search
   * @return a list of ObjectsDto
   */
  List<ObjectDto> getObjectsForRecoveryDate(LocalDateTime date);

  /**
   * Retrieves a list of all objects in the database with these types.
   *
   * @param types id of types to find
   * @return a list of ObjectsDto
   */
  List<ObjectDto> getObjectsSortedByTypes(List<Integer> types);

  /**
   * create an object and add it in the db.
   *
   * @param objectInput the input of the object
   * @return the created object
   */
  ObjectDto createObject(ObjectDto objectInput);

  /**
   * sort with dates of offered_dates.
   *
   * @param startDate startDate to sort
   * @param endDate   endDate to sort
   * @return the sorted list
   */
  List<ObjectDto> getObjectsBetweenTwoDate(LocalDateTime startDate, LocalDateTime endDate);
}
