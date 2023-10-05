package be.vinci.pae.domain.interfaces;

import jakarta.ws.rs.NotFoundException;
import java.util.List;

/**
 * This interface represents a user UCC in the system.
 */
public interface UserUCC {

  /**
   * Register a user to the system.
   *
   * @param userInput the user wanting to register
   * @return UserDto the newly registered user
   */
  UserDto register(UserDto userInput);

  /**
   * Connects the user to the system.
   *
   * @param email    the user email
   * @param password the user password
   * @return UserDto
   */
  UserDto login(String email, String password);

  /**
   * Keep the user connected using the id.
   *
   * @param id the user id
   * @return UserDto
   */
  UserDto getOneUserById(int id);

  /**
   * Returns a list of all users from DB.
   *
   * @return a list of UserDto objects
   * @throws NotFoundException if the users list is empty
   */
  List<UserDto> getAllUsers();

  /**
   * Returns a userDTO with the helper that has his confirmation been confirmed.
   *
   * @param id the id of the user to be set as helper
   * @return a userDTO is returned with update information
   * @throws NotFoundException if the helper was not found in the db
   */
  UserDto confirmHelperRegistration(int id);

  /**
   * Returns a userDTO with the Responsible that has his confirmation been confirmed.
   *
   * @param id the id of the user to be set as responsible
   * @return a userDTO is returned with update information
   * @throws NotFoundException if the helper was not found in the db
   */
  UserDto confirmResponsibleRegistration(int id);

  /**
   * Returns a userDTO with the new profil picture updated.
   *
   * @param id          the of the picture to be changed
   * @param pictureName the name of the picture
   * @return a userDTO is returned with update information
   * @throws NotFoundException if the helper was not found in the db
   */
  UserDto updateUserPicture(int id, String pictureName);

  /**
   * Update the information of a user.
   *
   * @param userInput the user wanting to register
   * @return a userDTO is returned with update information
   */
  UserDto updateUser(UserDto userInput);

}
