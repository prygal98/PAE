package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.AvailabilityDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface describe the data access Availabilities.
 */
public interface AvailabilityDAO {

  /**
   * Add a new availability to the db .
   *
   * @param availability date and  timeSlot of the availability that has to be added in db.
   * @return an AvailabilityDto representing the added availability.
   */
  AvailabilityDto createOneAvailability(AvailabilityDto availability);

  /**
   * Create a list of AvailabilityDto using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return one AvailabilityDto.
   * @throws SQLException sql exception.
   */
  List<AvailabilityDto> makeAvailabilities(ResultSet rs) throws SQLException;

  /**
   * Create a single AvailabilityDto using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return return the dto from the avaibility
   * @throws SQLException sql exception
   */
  AvailabilityDto makeOneAvailability(ResultSet rs) throws SQLException;

  /**
   * Retrieves a list of all Availabilities .
   *
   * @return a list of AvailabilitiesDto representing all the Availabilities.
   */
  List<AvailabilityDto> getAllAvailabilities();
}
