package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.utils.MyFatalException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface represents a user DAO in the system.
 */
public interface UserDAO {

  /**
   * Insert a registered user into the database, using the input provided by a client.
   *
   * @param userInput the input provided by a client.
   * @return the registered user as inserted into the database.
   */
  UserDto createOneUser(UserDto userInput);

  /**
   * method to read a user from the database. using the id.
   *
   * @param id the id of the requested user.
   * @return UserDto : the user received from the database.
   */
  UserDto getOneUserById(int id);

  /**
   * method to read a user from the database. using the email.
   *
   * @param email the email of the requested user.
   * @return UserDto : the user received from the database.
   */
  UserDto getOneUserByEmail(String email);

  /**
   * Retrieves a list of all users in the database.
   *
   * @return a list of UserDto representing all users in the database
   */
  List<UserDto> getAllUsers();

  /**
   * Make a list of UserDTO using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return a list of UserDTO.
   * @throws SQLException an exception from the resultSet.
   */
  List<UserDto> makeUsers(ResultSet rs) throws SQLException;

  /**
   * Make a single UserDTO using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return a UserDTO.
   * @throws SQLException an exception from the resultSet.
   */
  UserDto makeOneUser(ResultSet rs) throws SQLException;

  /**
   * Update a user.
   *
   * @param user the user as should be updated in the database.
   * @return the updated user from the database.
   * @throws MyFatalException an exception englobing an SQLException.
   */
  UserDto updateUser(UserDto user) throws MyFatalException;


}
