package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.Availability;
import be.vinci.pae.domain.interfaces.Object;
import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.ws.rs.NotAllowedException;
import java.time.LocalDateTime;

/**
 * Represents an object entity in the application.
 */
class ObjectImpl implements Object, ObjectDto {

  private int id;
  private String name;
  private String description;
  private String picturePath;
  private STATE state;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = be.vinci.pae.api.jsonutils.TimePattern.PATTERN)
  private LocalDateTime offerDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = be.vinci.pae.api.jsonutils.TimePattern.PATTERN)
  private LocalDateTime acceptDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = be.vinci.pae.api.jsonutils.TimePattern.PATTERN)
  private LocalDateTime recoveryDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = be.vinci.pae.api.jsonutils.TimePattern.PATTERN)
  private LocalDateTime storeDepositDate;
  private double sellingPrice;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = be.vinci.pae.api.jsonutils.TimePattern.PATTERN)
  private LocalDateTime sellingDate;
  private int typeObject;
  private int availabilityId;
  private Availability availability;
  private int ownerId;
  private String phoneOwner;
  private String refusalReason;
  private UserDto owner;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = be.vinci.pae.api.jsonutils.TimePattern.PATTERN)
  private LocalDateTime withdrawDate;

  public ObjectImpl() {

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
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getPicturePath() {
    return picturePath;
  }

  @Override
  public void setPicturePath(String picturePath) {
    this.picturePath = picturePath;
  }

  @Override
  public STATE getState() {
    return state;
  }

  @Override
  public void setState(STATE state) {
    this.state = state;
  }

  @Override
  public LocalDateTime getOfferDate() {
    return offerDate;
  }

  @Override
  public void setOfferDate(LocalDateTime offerDate) {
    this.offerDate = offerDate;
  }

  @Override
  public LocalDateTime getAcceptDate() {
    return acceptDate;
  }

  @Override
  public void setAcceptDate(LocalDateTime acceptDate) {
    this.acceptDate = acceptDate;
  }

  @Override
  public LocalDateTime getRecoveryDate() {
    return recoveryDate;
  }

  @Override
  public void setRecoveryDate(LocalDateTime recoveryDate) {
    this.recoveryDate = recoveryDate;
  }

  @Override
  public LocalDateTime getStoreDepositDate() {
    return storeDepositDate;
  }

  @Override
  public void setStoreDepositDate(LocalDateTime storeDepositDate) {
    this.storeDepositDate = storeDepositDate;
  }

  @Override
  public double getSellingPrice() {
    return sellingPrice;
  }

  @Override
  public void setSellingPrice(double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  @Override
  public LocalDateTime getSellingDate() {
    return sellingDate;
  }

  @Override
  public void setSellingDate(LocalDateTime sellingDate) {
    this.sellingDate = sellingDate;
  }

  @Override
  public int getTypeObject() {
    return typeObject;
  }

  @Override
  public void setTypeObject(int typeObject) {
    this.typeObject = typeObject;
  }

  @Override
  public int getAvailabilityId() {
    return this.availabilityId;
  }

  @Override
  public void setAvailabilityId(int availabilityId) {
    this.availabilityId = availabilityId;
  }

  @Override
  public Availability getAvailability() {
    return availability;
  }

  @Override
  public void setAvailability(Availability availability) {
    this.availability = availability;
  }

  @Override
  public int getOwnerId() {
    return this.ownerId;
  }

  @Override
  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }

  @Override
  public String getPhoneOwner() {
    return phoneOwner;
  }

  @Override
  public void setPhoneOwner(String phoneOwner) {
    this.phoneOwner = phoneOwner;
  }

  @Override
  public String getRefusalReason() {
    return refusalReason;
  }

  @Override
  public void setRefusalReason(String refusalReason) {
    this.refusalReason = refusalReason;
  }

  @Override
  public UserDto getOwner() {
    return this.owner;
  }

  @Override
  public void setOwner(UserDto ownerName) {
    this.owner = ownerName;
  }

  @Override
  public void setObjectStateConfirmed() throws NotAllowedException {
    if (this.getState() != STATE.OFFERED) {
      throw new NotAllowedException("Object need to be in OFFERED to be changed to CONFIRMED");
    }
    this.state = STATE.CONFIRMED;
    this.acceptDate = LocalDateTime.now();
  }

  @Override
  public void setObjectStateDenied(String refusalReason) throws NotAllowedException {
    if (this.getState() != STATE.OFFERED) {
      throw new NotAllowedException("Object need to be in OFFERED to be changed to DENIED");
    }
    this.state = STATE.DENIED;
    this.refusalReason = refusalReason;
  }

  @Override
  public void setObjectStateWorkshop() throws NotAllowedException {
    if (this.getState() != STATE.CONFIRMED) {
      throw new NotAllowedException("Object need to be in CONFIRMED to be changed to WORKSHOP");
    }
    this.state = STATE.WORKSHOP;
    this.recoveryDate = LocalDateTime.now();
  }

  @Override
  public void setObjectStateStore() throws NotAllowedException {
    if (this.getState() != STATE.CONFIRMED && this.getState() != STATE.WORKSHOP) {
      throw new NotAllowedException(
          "Object need to be in CONFIRMED or WORKSHOP to be changed to STORE");
    }
    if (this.getRecoveryDate() == null) {
      this.recoveryDate = LocalDateTime.now();
    }
    this.state = STATE.STORE;
    this.storeDepositDate = LocalDateTime.now();

  }

  @Override
  public void setObjectStateSale(double sellingPrice) throws NotAllowedException {
    if (sellingPrice <= 0) {
      throw new NotAllowedException("Selling price for this object should be strictly positive");
    }
    if (this.getState() != STATE.STORE) {
      throw new NotAllowedException("Object need to be in STORE to be changed to SALE");
    }
    this.state = STATE.SALE;
    this.sellingPrice = sellingPrice;
  }

  @Override
  public void setObjectStateSold() throws NotAllowedException {
    if (this.getState() != STATE.SALE) {
      throw new NotAllowedException("Object need to be in SALE to be changed to SOLD");
    }
    this.state = STATE.SOLD;
    this.sellingDate = LocalDateTime.now();
  }

  @Override
  public void setObjectStateSoldWithPrice(double sellingPrice) throws NotAllowedException {
    if (this.getState() != STATE.STORE) {
      throw new NotAllowedException("Object need to be in STORE to be changed to SOLD");
    }
    if (sellingPrice <= 0) {
      throw new NotAllowedException("Selling price for this object should be strictly positive");
    }
    this.state = STATE.SOLD;
    this.sellingPrice = sellingPrice;
    this.sellingDate = LocalDateTime.now();
  }


  @Override
  public void setObjectStateWithdrawn() throws NotAllowedException {
    if (this.getState() == STATE.SOLD || this.getState() == STATE.DENIED) {
      throw new NotAllowedException(
          "Object in state " + this.getState() + " can't be changed to WITHDRAWN");
    }
    this.state = STATE.WITHDRAWN;
  }

  @Override
  public LocalDateTime getWithdrawDate() {
    return withdrawDate;
  }

  @Override
  public void setWithdrawDate(LocalDateTime withdrawDate) {
    this.withdrawDate = withdrawDate;
  }
}
