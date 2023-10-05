package be.vinci.pae.domain.interfaces;

import be.vinci.pae.domain.interfaces.User.UserRole;
import java.time.LocalDateTime;

/**
 * The UserDto interface represents a user in the application.
 */
public interface UserDto {

  /**
   * get the attribute 'id' of this user.
   *
   * @return the id (in the database) of the user.
   */
  int getId();

  /**
   * Set the attribute 'id' (of the database) of this user.
   *
   * @param id this user.
   */
  void setId(int id);

  /**
   * get the attribute 'lastname' of this user.
   *
   * @return the lastname of this user.
   */
  String getLastname();

  /**
   * Set the attribute 'lastname' of this user.
   *
   * @param lastname the lastname of this user.
   */
  void setLastname(String lastname);

  /**
   * get the attribute 'firstname' of this user.
   *
   * @return the firstname of this user.
   */
  String getFirstname();

  /**
   * Set the attribute 'firstname' of this user.
   *
   * @param firstname the firstname of this user.
   */
  void setFirstname(String firstname);

  /**
   * get the attribute 'email' of this user.
   *
   * @return the email of this user.
   */
  String getEmail();

  /**
   * Set the attribute 'email' of this user.
   *
   * @param email the email of this user.
   */
  void setEmail(String email);


  /**
   * get the attribute 'Gsm' of this user.
   *
   * @return the Gsm of this user.
   */
  String getGsm();

  /**
   * Set the attribute 'gsm' of this user.
   *
   * @param gsm the gsm of this user.
   */
  void setGsm(String gsm);

  /**
   * get the attribute 'password' of this user.
   *
   * @return the password of this user.
   */
  String getPassword();

  /**
   * Set the attribute 'password' of this user.
   *
   * @param mdp the password of this user.
   */
  void setPassword(String mdp);

  /**
   * get the date when this user registered.
   *
   * @return the date when this user registered.
   */
  LocalDateTime getRegistrationDate();

  /**
   * Set the date when this user registered.
   *
   * @param registrationDate the date when this user registered.
   */
  void setRegistrationDate(LocalDateTime registrationDate);

  /**
   * get the role of this user.
   *
   * @return the role of this user. Defined by the ENUM UserRole in User.
   */
  UserRole getUserRole();

  /**
   * Set the role of this user.
   *
   * @param userRole the role of this user. Defined by the ENUM UserRole in User.
   */
  void setUserRole(UserRole userRole);

  /**
   * get the url of the profile picture of this user.
   *
   * @return the url of the profile picture of this user.
   */
  String getPicture();

  /**
   * Set the url of the profile picture of this user.
   *
   * @param picture the url of the profile picture of this user.
   */
  void setPicture(String picture);


}