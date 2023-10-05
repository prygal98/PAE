package be.vinci.pae.domain.interfaces;

import be.vinci.pae.domain.interfaces.Object.STATE;
import jakarta.ws.rs.NotAllowedException;
import java.time.LocalDateTime;

/**
 * This interface describe the data transfer object class for Objects.
 */
public interface ObjectDto {

  /**
   * get the attribute 'id' of this object.
   *
   * @return the id.
   */
  int getId();

  /**
   * Set the attribute 'id' of this object.
   *
   * @param id the id in the database.
   */
  void setId(int id);

  /**
   * get the attribute 'name' of this object.
   *
   * @return the name.
   */
  String getName();

  /**
   * Set the attribute 'name' of this object.
   *
   * @param name the name of the object.
   */
  void setName(String name);

  /**
   * get the attribute 'description' of this object.
   *
   * @return the description.
   */
  String getDescription();

  /**
   * Set the attribute 'description' of this object.
   *
   * @param description the description of the object.
   */
  void setDescription(String description);

  /**
   * getter for object's picture path.
   *
   * @return picture path
   */
  String getPicturePath();

  /**
   * setter for picture path of the object.
   *
   * @param picturePath to set
   */
  void setPicturePath(String picturePath);

  /**
   * get the state of the object.
   *
   * @return actual state (enum)
   */
  STATE getState();

  /**
   * Set the attribute 'state' of this object.
   *
   * @param state the state of the object (ie: ACCEPTED, OFFERED,...) given by the ENUM in Object.
   */
  void setState(STATE state);

  /**
   * get the attribute 'acceptDate' of this object.
   *
   * @return the acceptDate.
   */
  LocalDateTime getAcceptDate();

  /**
   * Set the attribute 'acceptDate' of this object.
   *
   * @param acceptDate the date when the object was placed in store.
   */
  void setAcceptDate(LocalDateTime acceptDate);

  /**
   * get the attribute 'offerDate' of this object.
   *
   * @return the offerDate.
   */
  LocalDateTime getOfferDate();

  /**
   * Set the attribute 'offerDate' of this object.
   *
   * @param offerDate the date when the object was placed in store.
   */
  void setOfferDate(LocalDateTime offerDate);

  /**
   * get the attribute 'recovery_date' of this object.
   *
   * @return the storeDepositDate.
   */
  LocalDateTime getRecoveryDate();

  /**
   * Set the attribute 'recovery_date' of this object.
   *
   * @param recoveryDate the date when the object was placed in store.
   */
  void setRecoveryDate(LocalDateTime recoveryDate);

  /**
   * get the attribute 'storeDepositDate' of this object.
   *
   * @return the storeDepositDate.
   */
  LocalDateTime getStoreDepositDate();

  /**
   * Set the attribute 'storeDepositDate' of this object.
   *
   * @param storeDepositDate the date when the object was placed in store.
   */
  void setStoreDepositDate(LocalDateTime storeDepositDate);

  /**
   * get the attribute 'sellingPrice' of this object.
   *
   * @return the sellingPrice.
   */
  double getSellingPrice();

  /**
   * Set the attribute 'sellingPrice' of this object.
   *
   * @param sellingPrice the price of the object.
   */
  void setSellingPrice(double sellingPrice);

  /**
   * get the attribute 'sellingDate' of this object.
   *
   * @return the sellingDate.
   */
  LocalDateTime getSellingDate();

  /**
   * Set the attribute 'sellingDate' of this object.
   *
   * @param sellingDate the date when the object was sold.
   */
  void setSellingDate(LocalDateTime sellingDate);

  /**
   * get the attribute 'typeObject' of this object.
   *
   * @return the typeObject.
   */
  int getTypeObject();

  /**
   * Set the attribute 'typeObject' of this object.
   *
   * @param typeObject the type of object as a foreign key in the database.
   */
  void setTypeObject(int typeObject);

  /**
   * get the attribute 'availabilityId' of this object.
   *
   * @return the availabilityId.
   */
  int getAvailabilityId();

  /**
   * Set the attribute 'availabilityId' of this object.
   *
   * @param availabilityId the id (in the database) of the date when the object is given by a user.
   */
  void setAvailabilityId(int availabilityId);

  /**
   * get the attribute 'availability' of this object.
   *
   * @return the availability.
   */
  Availability getAvailability();

  /**
   * Set the attribute 'availability' of this object.
   *
   * @param availability the date when the object is given by a user.
   */
  void setAvailability(Availability availability);

  /**
   * get the attribute 'ownerId' of this object.
   *
   * @return the ownerId.
   */
  int getOwnerId();

  /**
   * Set the attribute 'ownerId' of this object.
   *
   * @param ownerId the id (in the database) of the user who is giving this object.
   */
  void setOwnerId(int ownerId);

  /**
   * get the phone number for the owner of the object.
   *
   * @return the phone number for the owner
   */
  String getPhoneOwner();

  /**
   * set the phone number of the owner.
   *
   * @param phoneOwner to set
   */
  void setPhoneOwner(String phoneOwner);

  /**
   * get the refusal reason the object.
   *
   * @return the refusal reason
   */
  String getRefusalReason();

  /**
   * Set the attribute 'refusalReason' of this object.
   *
   * @param refusalReason the reason the object was not accepted.
   */
  void setRefusalReason(String refusalReason);

  /**
   * get the owner of the object.
   *
   * @return the owner
   */
  UserDto getOwner();

  /**
   * Set the attribute 'owner' of this object.
   *
   * @param owner the user who is giving this object.
   */
  void setOwner(UserDto owner);

  /**
   * get the withdraw date of the object.
   *
   * @return the owner
   */
  LocalDateTime getWithdrawDate();

  /**
   * Set the object to withdraw.
   *
   * @param withdrawDate price that the object has been sold for.
   * @throws NotAllowedException if the object is in not in the required state.
   */
  void setWithdrawDate(LocalDateTime withdrawDate);

}
