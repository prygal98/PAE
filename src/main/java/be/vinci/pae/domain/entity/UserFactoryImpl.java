package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.domain.interfaces.UserFactory;

/**
 * UserFactoryImpl is the class used to create new Users.
 */
public class UserFactoryImpl implements UserFactory {


  @Override
  public UserDto getUser() {
    return new UserImpl();
  }

}
