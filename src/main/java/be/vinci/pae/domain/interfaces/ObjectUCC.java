package be.vinci.pae.domain.interfaces;

import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This interface describe the UCC Class for objects.
 */
public interface ObjectUCC {

  /**
   * Accepts an object proposition by changing its state to CONFIRMED.
   *
   * @param id the ID of the object to update
   * @return the updated ObjectDto with the new state and owner name
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object's current state is not OFFERED
   */
  ObjectDto acceptObjectProposition(int id);

  /**
   * Denies the state of an object with the given ID and updates the object's state to DENIED.
   *
   * @param id            the ID of the object to update.
   * @param refusalReason the reason for the refusal of the object.
   * @return the updated ObjectDto
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object's current state is not OFFERED
   */
  ObjectDto denyObjectState(int id, String refusalReason);

  /**
   * Changes the state of an object to "Workshop" for the given object ID.
   *
   * @param id the ID of the object to update
   * @return the updated object DTO
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object is not in the correct state to be changed to
   *                             "Workshop"
   */
  ObjectDto workshopObjectState(int id);

  /**
   * Changes the state of an object to "Store" for the given object ID.
   *
   * @param id the ID of the object to update
   * @return the updated object DTO
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object is not in the correct state to be changed to "Store"
   */
  ObjectDto storeObjectState(int id);

  /**
   * Changes the state of an object to "Sale" for the given object ID.
   *
   * @param id           the ID of the object to update
   * @param sellingPrice the price of the object to update
   * @return the updated object DTO
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object is not in the correct state to be changed to "Sale"
   */
  ObjectDto saleObjectState(int id, double sellingPrice);

  /**
   * Changes the state of an object to "Sold" for the given object ID.
   *
   * @param id the ID of the object to update
   * @return the updated object DTO
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object is not in the correct state to be changed to "Sold"
   */
  ObjectDto soldObjectState(int id);

  /**
   * Changes the state of an object to "WITHDRAWN" for the given object ID.
   *
   * @param id the ID of the object to update
   * @return the updated object DTO
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object is not in the correct state to be changed to
   *                             "WITHDRAWN"
   */
  ObjectDto withdrawnObjectState(int id);

  /**
   * Returns a list of all ObjectDto objects from DB.
   *
   * @return a list of ObjectDto objects
   * @throws NotFoundException if the object list is empty
   */
  List<ObjectDto> getAllObjects();

  /**
   * Returns an ObjectDto object with the provided ID and sets the owner name.
   *
   * @param id the ID of the object to retrieve
   * @return the ObjectDto object with the provided ID
   * @throws NotFoundException if the object with the given ID does not exist
   */
  ObjectDto getOneObjectById(int id);

  /**
   * Returns all the objects from the id user.
   *
   * @param id the id of the user from who we get the objects
   * @return a list of all the object of the user
   */
  List<ObjectDto> getAllObjectsFromOneUser(int id);

  /**
   * Returns all the objects for between min and max.
   *
   * @param priceMin the min price requested.
   * @param priceMax the max price requested.
   * @return a list of all the object for these prices.
   */
  List<ObjectDto> getObjectsBetweenTwoPrice(int priceMin, int priceMax);

  /**
   * Returns all the objects recovery at the date.
   *
   * @param date the date wanted for the object
   * @return a list of all the object for this date
   */
  List<ObjectDto> getObjectsForRecoveryDate(LocalDateTime date);

  /**
   * Returns all the objects for specific types (id).
   *
   * @param types types to sort.
   * @return a list of all the object for these types.
   */
  List<ObjectDto> getObjectsSortedByTypes(List<Integer> types);

  /**
   * Update the picture for an object.
   *
   * @param id          object
   * @param pictureName name of the picture in img directory
   * @return object DTO of updated object
   */
  ObjectDto updateObjectPicture(int id, String pictureName);


  /**
   * Returns a list of all the objects type objects from DB.
   *
   * @return a list of objectTypes
   * @throws NotFoundException if the object list is empty
   */
  List<TypeObjectDto> getAllTypeObjects();

  /**
   * Returns the created object .
   *
   * @param objectInput the object input
   * @return the created object from the db
   */
  ObjectDto createObject(ObjectDto objectInput);

  /**
   * Returns the number of sold items.
   *
   * @return number of sold items.
   */
  int getNumberOfSoldObjects();

  /**
   * Returns the number of proposed objects by year.
   *
   * @return a map : Year, number of objects
   */
  Map<Integer, Integer> getNumberOfProposedObjectsByPeriod();

  /**
   * Changes the state of an object to "Sold" for the given object ID.
   *
   * @param objectId     the ID of the object to update
   * @param sellingPrice the price of the object to update
   * @return the updated object DTO
   * @throws NotFoundException   if the object with the given ID does not exist
   * @throws NotAllowedException if the object is not in the correct state to be changed to "Sold"
   */
  ObjectDto soldInStore(int objectId, double sellingPrice);


  /**
   * Returns all the objects between startDate and endDate , it takes startDate and endDate of the
   * offered_date .
   *
   * @param startDate the min price requested.
   * @param endDate   the max price requested.
   * @return a list of all the object between this two dates
   */
  List<ObjectDto> getObjectsBetweenTwoDate(LocalDateTime startDate, LocalDateTime endDate);

  /**
   * Update the information of an object.
   *
   * @param objectDto the object that the user want to update the information on.
   * @return the object that has been updated.
   */
  ObjectDto updateObject(ObjectDto objectDto);
}
