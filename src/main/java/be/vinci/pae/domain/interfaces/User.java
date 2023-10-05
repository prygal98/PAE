package be.vinci.pae.domain.interfaces;

import jakarta.ws.rs.NotAuthorizedException;

/**
 * This interface represents a user entity in the system.
 */
public interface User extends UserDto {

  /**
   * Checks if the provided password matches the user's password.
   *
   * @param password the clear password to be compared to the hashed password of this user.
   * @return true if the passwords match, false otherwise.
   */
  boolean checkPassword(String password);

  /**
   * Hash the password of a user.
   *
   * @param password the clear password of a user.
   * @return the hashed password of the user.
   */
  String hashPassword(String password);

  /**
   * Make a regular user into a helper.
   *
   * @throws NotAuthorizedException if the current user's role is already HELPER or RESPONSIBLE.
   */
  void makeHelper() throws NotAuthorizedException;

  /**
   * Make a regular user into a responsible.
   *
   * @throws NotAuthorizedException if the current user's role is already HELPER or RESPONSIBLE.
   */
  void makeResponsible() throws NotAuthorizedException;

  /**
   * Add the required field, and change what needs to be changed to create a new user.
   */
  void makeNewUser();

  /**
   * The different types of users in the system.
   */
  enum UserRole {
    /**
     * RESPONSIBLE are the owner of the app.
     */
    RESPONSIBLE,
    /**
     * HELPER are helping RESPONSIBLE.
     */
    HELPER,
    /**
     * User are basic user without permissions.
     */
    USER
  }

}
