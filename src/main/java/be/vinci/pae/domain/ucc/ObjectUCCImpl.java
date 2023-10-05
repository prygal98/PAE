package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.interfaces.Object;
import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.ObjectUCC;
import be.vinci.pae.domain.interfaces.TypeObjectDto;
import be.vinci.pae.services.TransactionServices;
import be.vinci.pae.services.dao.AvailabilityDAO;
import be.vinci.pae.services.dao.ObjectDAO;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The ObjectUCCImpl class implements the ObjectUCC interface. It's provides methods to update the
 * state of objects in the system.
 */
public class ObjectUCCImpl implements ObjectUCC {

  //private final String unidentifiedOwner = "unidentified owner";
  @Inject
  private ObjectDAO objectDAO;

  @Inject
  private AvailabilityDAO availabilityDAO;

  @Inject
  private TransactionServices transaction;

  @Override
  public ObjectDto acceptObjectProposition(int id) {

    Object oldObject = (Object) getOneObjectById(id);

    transaction.start();

    try {
      oldObject.setObjectStateConfirmed();
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method acceptObjectProposition");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method acceptObjectProposition: " + e.getMessage());
      transaction.rollback();
      throw e;
    }

  }

  @Override
  public ObjectDto denyObjectState(int id, String refusalReason) {

    Object oldObject = (Object) getOneObjectById(id);

    transaction.start();

    try {
      oldObject.setObjectStateDenied(refusalReason);
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method denyObjectState");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method denyObjectState: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public ObjectDto workshopObjectState(int id) {

    Object oldObject = (Object) getOneObjectById(id);

    transaction.start();

    try {
      oldObject.setObjectStateWorkshop();
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method workshopObjectState");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method workshopObjectState: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public ObjectDto storeObjectState(int id) {

    Object oldObject = (Object) getOneObjectById(id);

    transaction.start();

    try {
      oldObject.setObjectStateStore();
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method storeObjectState");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method storeObjectState: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public ObjectDto saleObjectState(int id, double sellingPrice) {

    Object oldObject = (Object) getOneObjectById(id);

    transaction.start();

    try {
      oldObject.setObjectStateSale(sellingPrice);
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method saleObjectState");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method saleObjectState: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public ObjectDto soldObjectState(int id) {

    Object oldObject = (Object) getOneObjectById(id);

    transaction.start();

    try {
      oldObject.setObjectStateSold();
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method soldObjectState");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method soldObjectState: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public ObjectDto withdrawnObjectState(int id) {

    Object oldObject = (Object) getOneObjectById(id);

    transaction.start();

    try {
      oldObject.setObjectStateWithdrawn();
      oldObject.setWithdrawDate(LocalDateTime.now());
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method withdrawnObjectState");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method withdrawnObjectState: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public List<ObjectDto> getAllObjects() {

    transaction.start();

    try {
      List<ObjectDto> objects = objectDAO.getAllObjects();

      if (objects.isEmpty()) {
        MyLogger.warning("The object list is empty in getAllObjects");
        throw new NotFoundException("The object list is empty.");
      }

      transaction.commit();
      return objects;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getAllObjects");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getAllObjects: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public ObjectDto getOneObjectById(int id) {

    transaction.start();

    try {
      ObjectDto objectDto = objectDAO.getOneObjectById(id);

      if (objectDto == null) {
        MyLogger.warning("Object not found with the given ID in getOneObjectById");
        throw new NotFoundException("Object not found with the given ID.");
      }

      transaction.commit();
      return objectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getOneObjectById");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getOneObjectById: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }


  @Override
  public ObjectDto updateObjectPicture(int id, String pictureName) {

    transaction.start();

    try {
      Object oldObject = (Object) objectDAO.getOneObjectById(id);

      if (oldObject == null) {
        MyLogger.warning("object not found in updateObjectPicture");
        throw new NotFoundException("object not found");
      }
      oldObject.setPicturePath(pictureName);
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method updateObjectPicture");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method updateObjectPicture: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public List<ObjectDto> getAllObjectsFromOneUser(int id) {

    transaction.start();

    try {
      List<ObjectDto> objects = objectDAO.getAllObjectsFromOneUser(id);

      if (objects.isEmpty()) {
        MyLogger.warning("The object list is empty in getAllObjectsFromOneUser");
        throw new NotFoundException("The object list is empty.");
      }

      transaction.commit();
      return objects;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getAllObjectsFromOneUser");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getAllObjectsFromOneUser: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public List<ObjectDto> getObjectsForRecoveryDate(LocalDateTime date) {

    transaction.start();

    try {
      List<ObjectDto> objects = objectDAO.getObjectsForRecoveryDate(date);

      if (objects.isEmpty()) {
        MyLogger.warning("The object list is empty in getObjectsForRecoveryDate");
        throw new NotFoundException("The object list is empty.");
      }

      transaction.commit();
      return objects;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getObjectsForRecoveryDate");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getObjectsForRecoveryDate: " + e.getMessage());
      transaction.rollback();
      throw e;
    }


  }

  @Override
  public List<ObjectDto> getObjectsSortedByTypes(List<Integer> types) {

    transaction.start();

    try {
      List<ObjectDto> objects = objectDAO.getObjectsSortedByTypes(types);

      if (objects.isEmpty()) {
        MyLogger.warning("The object list is empty in getObjectsSortedByTypes");
        throw new NotFoundException("The object list is empty.");
      }

      transaction.commit();
      return objects;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getObjectsSortedByTypes");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getObjectsSortedByTypes: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public List<ObjectDto> getObjectsBetweenTwoPrice(int priceMin, int priceMax) {

    transaction.start();

    try {
      List<ObjectDto> objects = objectDAO.getObjectsBetweenTwoPrice(priceMin, priceMax);

      if (objects.isEmpty()) {
        MyLogger.warning("The object list is empty in getObjectsBetweenTwoPrice");
        throw new NotFoundException("The object list is empty.");
      }

      transaction.commit();
      return objects;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getObjectsBetweenTwoPrice");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getObjectsBetweenTwoPrice: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public Map<Integer, Integer> getNumberOfProposedObjectsByPeriod() {

    transaction.start();
    try {
      Map<Integer, Integer> nbrPerYear = objectDAO.getNumberOfProposedObjectsByPeriod();
      transaction.commit();

      if (nbrPerYear.isEmpty()) {
        throw new NotFoundException("The map is empty.");
      }
      return nbrPerYear;
    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getNumberOfProposedObjectsByPeriod");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getNumberOfProposedObjectsByPeriod: " + e.getMessage());
      transaction.rollback();
      throw e;
    }

  }

  @Override
  public ObjectDto soldInStore(int objectId, double sellingPrice) {
    Object oldObject = (Object) getOneObjectById(objectId);

    transaction.start();

    try {
      oldObject.setObjectStateSoldWithPrice(sellingPrice);
      ObjectDto updatedObjectDto = objectDAO.updateOneObject(oldObject);

      transaction.commit();
      return updatedObjectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method soldInStore");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method soldInStore: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public List<ObjectDto> getObjectsBetweenTwoDate(LocalDateTime startDate, LocalDateTime endDate) {
    transaction.start();
    try {
      List<ObjectDto> objects = objectDAO.getObjectsBetweenTwoDate(startDate, endDate);

      if (objects.isEmpty()) {
        MyLogger.warning("The object list is empty in getObjectsBetweenTwoPrice");
        throw new NotFoundException("The object list is empty.");
      }

      transaction.commit();
      return objects;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getObjectsBetweenTwoPrice");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getObjectsBetweenTwoDate: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public ObjectDto updateObject(ObjectDto objectDto) {
    // GET USER
    transaction.start();
    try {
      ObjectDto objectUpdate = objectDAO.updateOneObject(objectDto);

      // CHECK UPDATE HAS BEEN DONE
      if (objectUpdate == null) {
        MyLogger.warning("a problem occurred when trying to "
            + "update the information of the object because objectUpdate is null");
        throw new NotFoundException("a problem occurred when trying to "
            + "update the information of the object because objectUpdate is null");
      }

      // RETURN USERS
      transaction.commit();
      return objectDto;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method updateObject");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method updateObject: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }


  @Override
  public List<TypeObjectDto> getAllTypeObjects() {

    transaction.start();

    try {
      List<TypeObjectDto> types = objectDAO.getAllTypesObject();

      if (types.isEmpty()) {
        MyLogger.warning("The object type list is empty in getAllTypeObjects");
        throw new NotFoundException("The object type list is empty.");
      }

      transaction.commit();
      return types;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getAllTypeObjects");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getAllTypeObjects: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }


  @Override
  public ObjectDto createObject(ObjectDto objectInput) {
    transaction.start();
    try {
      ObjectDto objectDto = objectDAO.createObject(objectInput);
      if (objectDto == null) {
        MyLogger.warning("objectDAO.createObject returns null in method createObject");
        throw new NotFoundException("objectDAO.createObject returns null in method createObject");
      }
      transaction.commit();
      return objectDto;
    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method createObject");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method createObject: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public int getNumberOfSoldObjects() {
    transaction.start();
    try {
      int numberSold = objectDAO.getNumberOfSoldObjects();
      transaction.commit();
      return numberSold;
    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getNumberOfSoldObjects");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getNumberOfSoldObjects: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }
}