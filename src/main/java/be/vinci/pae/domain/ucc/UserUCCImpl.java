package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.interfaces.User;
import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.domain.interfaces.UserUCC;
import be.vinci.pae.services.TransactionServices;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;


/**
 * UserUCCImpl is the class used to give the logic ( business ).
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;

  @Inject
  private TransactionServices transaction;

  @Override
  public UserDto register(UserDto userInput) {

    transaction.start();

    try {
      User user = (User) userInput;
      user.makeNewUser();
      UserDto userReturn = userDAO.createOneUser(user);

      transaction.commit();
      return userReturn;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in register method of UserUCCImpl");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method register: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public UserDto login(String email, String password) {

    transaction.start();

    try {
      UserDto userDto = userDAO.getOneUserByEmail(email);

      // CHECK USER
      User user = (User) userDto;
      if (user == null || !user.checkPassword(password)) {
        MyLogger.warning("email or password incorrect in login");
        throw new NotAuthorizedException("email or password incorrect");
      }

      // RETURN USER
      transaction.commit();
      return userDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method login");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method login: " + e.getMessage());
      transaction.rollback();
      throw e;
    }


  }

  @Override
  public UserDto getOneUserById(int id) {

    transaction.start();

    try {
      UserDto user = userDAO.getOneUserById(id);

      // CHECK USER
      if (user == null) {
        MyLogger.warning("could not find user with id " + id + " in getOneUserById");
        throw new NotFoundException("could not find user with id " + id);
      }

      // RETURN USER
      transaction.commit();
      return user;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getOneUserById");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getOneUserById: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public UserDto confirmHelperRegistration(int id) {
    // GET USER
    User user = (User) getOneUserById(id);

    transaction.start();

    try {
      // MAKE USER A HELPER
      user.makeHelper();

      // UPDATE USER

      UserDto updatedUser = userDAO.updateUser(user);

      // CHECK UPDATE HAS BEEN DONE
      if (updatedUser == null) {
        MyLogger.warning("a problem occurred when trying to register this helper");
        throw new NotFoundException("a problem occurred when trying to register this helper");
      }

      // RETURN USER
      transaction.commit();
      return updatedUser;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method confirmHelperRegistration");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method confirmHelperRegistration: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public UserDto confirmResponsibleRegistration(int id) {
    // GET USER
    User newUser = (User) getOneUserById(id);

    transaction.start();

    try {
      // MAKE USER A HELPER
      newUser.makeResponsible();

      // UPDATE USER

      UserDto updatedUser = userDAO.updateUser(newUser);

      // CHECK UPDATE HAS BEEN DONE
      if (updatedUser == null) {
        MyLogger.warning("a problem occurred when trying to register this responsible");
        throw new NotFoundException("a problem occurred when trying to register this responsible");
      }

      // RETURN USER
      transaction.commit();
      return updatedUser;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method confirmResponsibleRegistration");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method confirmResponsibleRegistration: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public UserDto updateUserPicture(int id, String pictureName) {

    User oldUser = (User) getOneUserById(id);

    transaction.start();

    try {
      oldUser.setPicture(pictureName);
      UserDto updatedUserDto = userDAO.updateUser(oldUser);

      transaction.commit();
      return updatedUserDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method updateUserPicture");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method updateUserPicture: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public List<UserDto> getAllUsers() {
    // GET USERS
    transaction.start();

    try {
      List<UserDto> allUsers = userDAO.getAllUsers();

      // CHECK USERS
      if (allUsers.isEmpty()) {
        MyLogger.warning("The object list is empty in getAllUsers");
        throw new NotFoundException("The object list is empty.");
      }

      // RETURN USERS
      transaction.commit();
      return allUsers;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in getAllUsers method of UserUCCImpl");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getAllUsers: " + e.getMessage());
      transaction.rollback();
      throw e;
    }

  }

  @Override
  public UserDto updateUser(UserDto userInput) {

    // GET USER
    transaction.start();
    try {
      UserDto updateUser = userDAO.updateUser(userInput);

      // CHECK UPDATE HAS BEEN DONE
      if (updateUser == null) {
        MyLogger.warning("a problem occurred when trying to "
            + "update the information of the user because updateUser is null");
        throw new NotFoundException("a problem occurred when trying to "
            + "update the information of the user");
      }

      // RETURN USERS
      transaction.commit();
      return updateUser;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method updateUser");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method updateUser: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }
}