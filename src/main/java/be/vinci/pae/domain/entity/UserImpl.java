package be.vinci.pae.domain.entity;

import be.vinci.pae.api.jsonutils.TimePattern;
import be.vinci.pae.domain.interfaces.User;
import be.vinci.pae.domain.interfaces.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.ws.rs.NotAuthorizedException;
import java.time.LocalDateTime;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Represents a user entity in the application.
 */
class UserImpl implements User, UserDto {

  private int id;
  private String firstname;
  private String lastname;
  private String email;
  private String gsm;
  @JsonIgnore
  private String password;
  private String base64Image;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimePattern.PATTERN)
  private LocalDateTime registrationDate;
  private UserRole userRole;
  private String picture;

  public UserImpl() {

  }

  @Override
  public boolean checkPassword(String clearPassword) {
    return BCrypt.checkpw(clearPassword, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public void makeHelper() throws NotAuthorizedException {
    if (this.getUserRole() == UserRole.HELPER) {
      throw new NotAuthorizedException("user is already a helper");
    }
    if (this.getUserRole() == UserRole.RESPONSIBLE) {
      throw new NotAuthorizedException("user cannot be retrograded to helper");
    }
    this.setUserRole(UserRole.HELPER);
  }

  @Override
  public void makeResponsible() throws NotAuthorizedException {
    if (this.getUserRole() == UserRole.HELPER) {
      throw new NotAuthorizedException("user is already a helper");
    }
    if (this.getUserRole() == UserRole.RESPONSIBLE) {
      throw new NotAuthorizedException("user cannot be retrograded to helper");
    }
    this.setUserRole(UserRole.RESPONSIBLE);
  }

  @Override
  public void makeNewUser() {
    this.setUserRole(UserRole.USER);
    this.setRegistrationDate(LocalDateTime.now());
    this.setPassword(hashPassword(this.getPassword()));
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
  public String getFirstname() {
    return firstname;
  }

  @Override
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  @Override
  public String getLastname() {
    return lastname;
  }

  @Override
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getGsm() {
    return gsm;
  }

  @Override
  public void setGsm(String gsm) {
    this.gsm = gsm;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public LocalDateTime getRegistrationDate() {
    return registrationDate;
  }

  @Override
  public void setRegistrationDate(LocalDateTime registrationDate) {
    this.registrationDate = registrationDate;
  }

  @Override
  public UserRole getUserRole() {
    return userRole;
  }

  @Override
  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  @Override
  public String getPicture() {
    return picture;
  }

  @Override
  public void setPicture(String picturePath) {
    this.picture = picturePath;
  }

  public String getBase64Image() {
    return base64Image;
  }

  public void setBase64Image(String base64Image) {
    this.base64Image = base64Image;
  }


}