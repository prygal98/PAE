package be.vinci.pae.domain.interfaces;

/**
 * UserFactory is the abstract class used to create new Users.
 */
public interface UserFactory {

  /**
   * Get new user.
   *
   * @return new user
   */
  UserDto getUser();
}
