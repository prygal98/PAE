package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.Object.STATE;
import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.ObjectFactory;
import be.vinci.pae.domain.interfaces.TypeObjectDto;
import be.vinci.pae.domain.interfaces.TypeObjectFactory;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class provides access to object data in the database.
 */
public class ObjectDAOImpl implements ObjectDAO {

  private final String selectSql = " SELECT o.*, u.*, t.*, av.* "
      + " FROM project.objects o ";
  private final String joinSql = " LEFT JOIN project.registered_users u "
      + "    ON o.owner_id = u.id_user "
      + " JOIN project.object_types t "
      + "    ON o.type_object = t.id_type "
      + " JOIN project.availability_dates av "
      + "    ON o.availability = av.id_availability ";

  private final String[] tableFields = {"name", "description", "picture", "state", "offer_date",
      "accept_date", "recovery_date", "store_deposit_date", "selling_price", "selling_date",
      "type_object", "availability", "phone_owner",
      "refusal_reason", "withdraw_date"}; // does not contain "owner_id"

  @Inject
  private ObjectFactory objectFactory;

  @Inject
  private UserDAO userDAO; // TODO trouver qqch de mieux si possible

  @Inject
  private AvailabilityDAO availabilityDAO; // TODO trouver qqch de mieux si possible

  @Inject
  private TypeObjectFactory typeObjectFactory;

  @Inject
  private DalServices dal;

  /* CREATE */
  @Override
  public ObjectDto createObject(ObjectDto objectInput) {
    // QUERY
    String query;

    if (objectInput.getOwnerId() > 0) {
      query = "INSERT INTO project.objects"
          + " ( name, description, picture, state, offer_date, "
          + " type_object, availability, phone_owner, owner_id ) "
          + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING * ;";
    } else {
      query = "INSERT INTO project.objects"
          + " ( name, description, picture, state, offer_date, "
          + " type_object, availability, phone_owner ) "
          + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING * ;";
    }

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(query)) {
      //int index = setPsObjectParam(objectInput, ps);

      ps.setString(1, objectInput.getName());
      ps.setString(2, objectInput.getDescription());
      ps.setString(3, objectInput.getPicturePath());
      ps.setString(4, String.valueOf(objectInput.getState()));
      ps.setTimestamp(5, Timestamp.valueOf(objectInput.getOfferDate()));
      ps.setInt(6, objectInput.getTypeObject());
      ps.setInt(7, objectInput.getAvailabilityId());
      ps.setString(8, objectInput.getPhoneOwner());

      if (objectInput.getOwnerId() > 0) {
        ps.setInt(9, objectInput.getOwnerId());
      }

      try (ResultSet rs = ps.executeQuery()) {
        return makeObjects(rs).get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getAllObjects method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public List<ObjectDto> getObjectsBetweenTwoDate(LocalDateTime startDate, LocalDateTime endDate) {
    String sql = "SELECT * FROM project.objects "
        + "WHERE offer_date BETWEEN ? AND ? ; ";
    try (PreparedStatement ps = dal.getPs(sql)) {

      ps.setDate(1, java.sql.Date.valueOf(startDate.toLocalDate()));
      ps.setDate(2, java.sql.Date.valueOf(endDate.toLocalDate()));

      try (ResultSet rs = ps.executeQuery()) {
        return makeObjects(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getObjectsBetweenTwoDate method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  /* READ */
  @Override
  public List<ObjectDto> getAllObjects() {
    // QUERY
    String sqlGet = selectSql + joinSql;

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlGet)) {
      try (ResultSet rs = ps.executeQuery()) {
        return makeObjects(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getAllTypesObject method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public ObjectDto getOneObjectById(int id) {
    // QUERY
    String sqlGet = selectSql + joinSql;
    sqlGet += " WHERE o.id_object = ? ";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlGet)) {

      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        List<ObjectDto> objects = makeObjects(rs);
        if (objects.isEmpty()) {
          MyLogger.warning("error in getOneObjectById method of ObjectDAOImpl because "
              + "object is empty");
          throw new NoSuchElementException();
        }
        return objects.get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getAllAvailabilities method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public List<ObjectDto> getAllObjectsFromOneUser(int id) {
    // QUERY
    String sql = " SELECT o.* FROM project.objects o,project.registered_users u "
        + "WHERE o.owner_id=u.id_user AND u.id_user = ? ";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sql)) {

      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        return makeObjects(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getAllObjectsFromOneUser method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public List<ObjectDto> getObjectsForRecoveryDate(LocalDateTime date) {
    // QUERY
    String sql = " SELECT o.* FROM project.objects o "
        + "WHERE o.recovery_date = ?";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sql)) {

      ps.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));

      try (ResultSet rs = ps.executeQuery()) {
        return makeObjects(rs);
      }
    } catch (SQLException e) {
      MyLogger.warning("error in getObjectsForRecoveryDate method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public List<ObjectDto> getObjectsBetweenTwoPrice(int priceMin, int priceMax) {
    String sql = " SELECT o.* FROM project.objects o "
        + "WHERE o.selling_price >= ? AND o.selling_price <= ?";

    try (PreparedStatement ps = dal.getPs(sql)) {

      ps.setInt(1, priceMin);
      ps.setInt(2, priceMax);

      try (ResultSet rs = ps.executeQuery()) {
        return makeObjects(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getObjectsBetweenTwoPrice method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public List<ObjectDto> getObjectsSortedByTypes(List<Integer> types) {
    // QUERY
    String sql = " SELECT o.* FROM project.objects o "
        + "WHERE ";
    for (int i = 0; i < types.size(); i++) {
      sql += "o.type_object = ? OR ";
    }

    sql = sql.substring(0, sql.length() - 4);

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sql)) {

      for (int i = 1; i <= types.size(); i++) {
        ps.setInt(i, types.get(i - 1));
      }

      try (ResultSet rs = ps.executeQuery()) {
        return makeObjects(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getObjectsSortedByTypes method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public int getNumberOfSoldObjects() {
    String sql = "SELECT count(o.id_object) FROM project.objects o "
        + "WHERE o.state = 'SOLD'";

    try (PreparedStatement ps = dal.getPs(sql)) {

      try (ResultSet rs = ps.executeQuery()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new MyFatalException(e);
    }
  }

  @Override
  public Map<Integer, Integer> getNumberOfProposedObjectsByPeriod() {
    String sql = "SELECT COALESCE(EXTRACT(YEAR FROM o.offer_date AT TIME ZONE 'UTC'),0) "
        + "as year, COUNT(*) AS nombre_objets "
        + "FROM project.objects o "
        + "WHERE o.state = 'OFFERED' "
        + "GROUP BY year";

    try (PreparedStatement ps = dal.getPs(sql)) {

      try (ResultSet rs = ps.executeQuery()) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        while (rs.next()) {
          int year = rs.getInt("year");
          int numberOfObjects = rs.getInt("nombre_objets");
          map.put(year, numberOfObjects);
        }

        return map;
      }
    } catch (SQLException e) {
      throw new MyFatalException(e);
    }
  }

  @Override
  public List<TypeObjectDto> getAllTypesObject() {
    // QUERY
    String sqlGet = "select t.* from project.object_types t";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlGet)) {
      try (ResultSet rs = ps.executeQuery()) {
        return makeTypesObjects(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getOneObjectById method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }


  /* UPDATE */
  @Override
  public ObjectDto updateOneObject(ObjectDto object) {
    // QUERY
    String sqlUpdate = " UPDATE project.objects o2 "
        + " SET "
        + String.join(" = ? , ", tableFields) + " = ? "
        + " FROM project.objects o "
        + joinSql
        + " WHERE o2.id_object = o.id_object "
        + " AND o.id_object = ? "
        + " RETURNING o2.*, u.*, t.*, av.* ";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlUpdate)) {

      int index = setPsObjectParam(object, ps);
      ps.setInt(index++, object.getId());

      try (ResultSet rs = ps.executeQuery()) {
        List<ObjectDto> objects = makeObjects(rs);
        if (objects.isEmpty()) {
          MyLogger.warning("error in getOneObjectById method of ObjectDAOImpl because "
              + "object is empty");
          throw new NoSuchElementException();
        }
        return objects.get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in updateOneObject method of ObjectDAOImpl");
      throw new MyFatalException(e);
    }
  }


  /*__Utils__*/
  // TODO solve problem with availability
  // TODO try to find a way to get the name of the object with a dto for objecttype

  @Override
  public List<ObjectDto> makeObjects(ResultSet rs) throws SQLException {
    List<ObjectDto> objects = new ArrayList<>();
    ObjectDto object;

    while (rs.next()) {
      object = makeOneObject(rs);
      objects.add(object);
    }

    return objects;
  }

  @Override
  public ObjectDto makeOneObject(ResultSet rs) throws SQLException {
    ObjectDto object = objectFactory.getObject();

    try {
      object.setId(rs.getInt("id_object"));

      object.setName(rs.getString("name"));
      object.setDescription(rs.getString("description"));
      if (rs.getString("picture") != null) {
        object.setPicturePath(rs.getString("picture"));
      }
      object.setState(STATE.valueOf(rs.getString("state")));

      if (rs.getDate("offer_date") != null) {
        object.setOfferDate(rs.getTimestamp("offer_date").toLocalDateTime());
      }

      if (rs.getDate("accept_date") != null) {
        object.setAcceptDate(rs.getTimestamp("accept_date").toLocalDateTime());
      }

      if (rs.getDate("store_deposit_date") != null) {
        object.setStoreDepositDate(rs.getTimestamp("store_deposit_date").toLocalDateTime());
      }
      if (rs.getDouble("selling_price") != 0) {
        object.setSellingPrice(rs.getDouble("selling_price"));
      }

      if (rs.getDate("selling_date") != null) {
        object.setSellingDate(rs.getTimestamp("selling_date").toLocalDateTime());
      }
      object.setTypeObject(rs.getInt("type_object"));

      // TODO setter in makeObjects
      // object.setAvailability(availabilityDAO.getOneAvailabilityById(idAvailability));
      object.setAvailabilityId(rs.getInt("availability"));

      if (rs.getInt("owner_id") != 0) {
        object.setOwnerId(rs.getInt("owner_id"));
      }

      object.setPhoneOwner(rs.getString("phone_owner"));

      if (rs.getString("refusal_reason") != null) {
        object.setRefusalReason(rs.getString("refusal_reason"));
      }

      // withdraw date
      if (rs.getString("withdraw_date") != null) {
        object.setWithdrawDate(rs.getTimestamp("withdraw_date").toLocalDateTime());
      }

    } catch (SQLException e) {
      MyLogger.warning("Error while creating ObjectDto from ResultSet: " + e.getMessage());
      throw e;
    }

    return object;
  }


  @Override
  public List<TypeObjectDto> makeTypesObjects(ResultSet rs) throws SQLException {
    List<TypeObjectDto> types = new ArrayList<>();
    TypeObjectDto type;

    while (rs.next()) {
      type = makeOneTypeObject(rs);
      types.add(type);
    }

    return types;
  }

  @Override
  public TypeObjectDto makeOneTypeObject(ResultSet rs) throws SQLException {
    TypeObjectDto type = typeObjectFactory.getType();

    type.setId(rs.getInt("id_type"));
    type.setObjectType(rs.getString("name"));

    return type;
  }

  /**
   * set every parameter for a PreparedStatement. Note that the order of parameter of the
   * PreparedStatement matters A LOT.
   *
   * @param object the object used to fill the parameter in the PreparedStatement.
   * @param ps     the PreparedStatement to which to wish to apply parameter.
   * @return la position du setter du PreparedStatement.
   * @throws SQLException if the method is not used correctly (eg: not the same amount of param
   *                      needed, or wrong order of param).
   */
  private int setPsObjectParam(ObjectDto object, PreparedStatement ps)
      throws SQLException {
    int index = 1;

    ps.setString(index++, object.getName());
    ps.setString(index++, object.getDescription());
    ps.setString(index++, object.getPicturePath());
    ps.setString(index++, object.getState().name());

    if (object.getOfferDate() != null) {
      ps.setTimestamp(index++, Timestamp.valueOf(object.getOfferDate()));
    } else {
      ps.setTimestamp(index++, null);
    }

    if (object.getAcceptDate() != null) {
      ps.setTimestamp(index++, Timestamp.valueOf(object.getAcceptDate()));
    } else {
      ps.setTimestamp(index++, null);
    }

    if (object.getRecoveryDate() != null) {
      ps.setTimestamp(index++, Timestamp.valueOf(object.getRecoveryDate()));
    } else {
      ps.setTimestamp(index++, null);
    }

    if (object.getStoreDepositDate() != null) {
      ps.setTimestamp(index++, Timestamp.valueOf(object.getStoreDepositDate()));
    } else {
      ps.setTimestamp(index++, null);
    }

    ps.setDouble(index++, object.getSellingPrice());
    if (object.getSellingDate() != null) {
      ps.setTimestamp(index++, Timestamp.valueOf(object.getSellingDate()));
    } else {
      ps.setTimestamp(index++, null);
    }
    ps.setInt(index++, object.getTypeObject());
    ps.setInt(index++, object.getAvailabilityId());
    ps.setString(index++, object.getPhoneOwner());
    ps.setString(index++, object.getRefusalReason());

    if (object.getWithdrawDate() != null) {
      ps.setTimestamp(index++, Timestamp.valueOf(object.getWithdrawDate()));
    } else {
      ps.setTimestamp(index++, null);
    }

    return index;
  }
}
