package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.AvailabilityDto;
import be.vinci.pae.domain.interfaces.AvailabilityFactory;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to access availability objects in database.
 */
public class AvailabilityDAOImpl implements AvailabilityDAO {

  //private final String[] tableFields = {"availability", "time_slot"};

  @Inject
  private AvailabilityFactory availabilityFactory;

  @Inject
  private DalServices dal;

  /* CREATE */
  @Override
  public AvailabilityDto createOneAvailability(AvailabilityDto availability) {

    // QUERY
    String query = "INSERT INTO project.availability_dates "
        + " ( availability, time_slot ) "
        + "VALUES ( ? , ? ) RETURNING *";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(query)) {
      setPsAvailabilityParam(availability, ps);
      try (ResultSet rs = ps.executeQuery()) {
        return makeAvailabilities(rs).get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("Request failed in createOneAvailability");
      throw new MyFatalException(e);
    }
  }

  /* READ */
  @Override
  public List<AvailabilityDto> getAllAvailabilities() {
    // QUERY
    String sqlGet = "SELECT a.* FROM project.availability_dates a";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlGet)) {
      try (ResultSet rs = ps.executeQuery()) {
        return makeAvailabilities(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("Request failed to add a new availability");
      throw new MyFatalException(e, "Request failed to add a new availability");
    }
  }

  /* Utils */
  @Override
  public List<AvailabilityDto> makeAvailabilities(ResultSet rs) throws SQLException {
    List<AvailabilityDto> availabilities = new ArrayList<>();
    AvailabilityDto availability;

    try {
      while (rs.next()) {
        availability = makeOneAvailability(rs);
        availabilities.add(availability);
      }
    } catch (SQLException e) {
      MyLogger.warning(
          "One of the column name is incorrect in AvailabilityDAO -> makeAvailability()");
      throw e;
    }
    return availabilities;
  }

  @Override
  public AvailabilityDto makeOneAvailability(ResultSet rs) throws SQLException {
    AvailabilityDto availability = availabilityFactory.getAvailability();

    try {

      availability.setId(rs.getInt("id_availability"));
      availability.setAvailability(rs.getTimestamp("availability").toLocalDateTime());
      availability.setTimeSlot(rs.getString("time_slot"));

    } catch (SQLException e) {
      MyLogger.warning(
          "One of the column name is incorrect in AvailabilityDAO -> makeAvailability()");
      throw e;
    }
    return availability;
  }

  /**
   * set every parameter for a PreparedStatement. Note that the order of parameter of the
   * PreparedStatement matter A LOT.
   *
   * @param availability the availability used to fill the parameter in the PreparedStatement.
   * @param ps           the PreparedStatement to which to wish to apply parameter.
   * @throws SQLException if the method is not used correctly (eg: not the same amount of param
   *                      needed, or wrong order of param).
   */
  private void setPsAvailabilityParam(AvailabilityDto availability, PreparedStatement ps)
      throws SQLException {
    if (availability.getAvailability() != null) {
      ps.setTimestamp(1, Timestamp.valueOf(availability.getAvailability()));
    } else {
      ps.setTimestamp(1, null);
    }
    ps.setString(2, availability.getTimeSlot());
  }
}
