package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.interfaces.AvailabilityDto;
import be.vinci.pae.domain.interfaces.AvailabilityUCC;
import be.vinci.pae.services.TransactionServices;
import be.vinci.pae.services.dao.AvailabilityDAO;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

/**
 * AvailabilityUCCImpl is the class used to give the logic ( business ).
 */
public class AvailabilityUCCImpl implements AvailabilityUCC {

  @Inject
  private AvailabilityDAO availabilityDAO;

  @Inject
  private TransactionServices transaction;

  @Override
  public AvailabilityDto addAvailability(AvailabilityDto availability) {

    transaction.start();

    try {
      AvailabilityDto returnAvailability = availabilityDAO.createOneAvailability(availability);

      transaction.commit();
      return returnAvailability;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method addAvailability");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method addAvailability: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public List<AvailabilityDto> getAllAvailabilities() {

    transaction.start();

    try {
      List<AvailabilityDto> availabilities = availabilityDAO.getAllAvailabilities();

      if (availabilities.isEmpty()) {
        MyLogger.warning("list availabilities empty in getAllAvailabilities");
        throw new NotFoundException("The availabilities list is empty.");
      }

      transaction.commit();
      return availabilities;
    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getAllAvailabilities");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method getAllAvailabilities: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }
}