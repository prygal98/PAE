package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.User.UserRole;
import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.domain.interfaces.UserFactory;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.strategies.GetByEmail;
import be.vinci.pae.services.strategies.GetById;
import be.vinci.pae.services.strategies.GetByStrategy;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class provides access to user data in the database.
 */
public class UserDAOImpl implements UserDAO {

  @Inject
  private UserFactory userFactory;

  @Inject
  private DalServices dal;

  private String[] tableFields = {"password", "email", "firstname", "lastname", "registration_date",
      "user_role", "picture", "phone_number"};

  /*__CRUD__*/
  /* CREATE */
  @Override
  public UserDto createOneUser(UserDto userInput) {
    // QUERY
    String query = " INSERT INTO project.registered_users "
        + " ("
        + String.join(", ", tableFields)
        + ") SELECT ?, ?, ?, ?, ?, ?, ? , ?"
        + " WHERE NOT EXISTS ("
        + "   SELECT * FROM project.registered_users WHERE email = ?"
        + " ) RETURNING * ;";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(query)) {
      setPsUserParam(userInput, ps);
      ps.setString(9, userInput.getEmail());
      try (ResultSet rs = ps.executeQuery()) {
        return makeUsers(rs).get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in createOneUser method of UserDAOImpl");
      throw new MyFatalException(e);
    }
  }

  /* READ */
  private List<UserDto> getUsersBy(GetByStrategy strategy) throws MyFatalException {
    // QUERY
    String query = "SELECT u.* "
        + "FROM project.registered_users u ";
    if (strategy != null) {
      query += "WHERE u." + strategy.getTableColumnName() + " = ?";
    }

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(query)) {
      if (strategy != null) {
        strategy.setParam(ps);
      }
      try (ResultSet rs = ps.executeQuery()) {
        return makeUsers(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in getUsersBy method of UserDAOImpl");
      throw new MyFatalException(e);
    }
  }

  @Override
  public UserDto getOneUserById(int id) {
    List<UserDto> users = getUsersBy(new GetById(id));
    if (users == null || users.isEmpty()) {
      MyLogger.warning("error in getOneUserById method of UserDAOImpl");
      throw new NoSuchElementException();
    }
    return users.get(0);
  }

  @Override
  public UserDto getOneUserByEmail(String email) {
    List<UserDto> users = getUsersBy(new GetByEmail(email));
    if (users == null || users.isEmpty()) {
      MyLogger.warning("error in getOneUserByEmail method of UserDAOImpl "
          + "because user is null or empty");
      throw new NoSuchElementException();
    }
    return users.get(0);
  }

  @Override
  public List<UserDto> getAllUsers() {
    return getUsersBy(null);
  }

  /* UPDATE */
  @Override
  public UserDto updateUser(UserDto user) throws MyFatalException {
    // QUERY to check if email is available
    String checkEmailQuery = "SELECT * FROM project.registered_users "
        + "WHERE email = ? AND id_user != ?";

    // Check if the new email is available
    try (PreparedStatement psCheckEmail = dal.getPs(checkEmailQuery)) {
      psCheckEmail.setString(1, user.getEmail());
      psCheckEmail.setInt(2, user.getId());
      try (ResultSet rsCheckEmail = psCheckEmail.executeQuery()) {
        if (rsCheckEmail.next()) {
          // email is already taken
          MyLogger.warning(
              "error in updateUser method of UserDAOImpl because email is already taken");
          throw new IllegalArgumentException("Email is already taken by another user");
        }
      }
    } catch (SQLException e) {
      MyLogger.warning(
          "error in updateUser method of UserDAOImpl while checking email availability");
      throw new MyFatalException(e);
    }

    // QUERY to update user
    String query = " UPDATE project.registered_users "
        + " SET "
        + String.join(" = ?, ", tableFields) + " = ? "
        + " WHERE id_user = ? " // TODO add version for optimistic lock in where clause
        + " RETURNING * ";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(query)) {
      setPsUserParam(user, ps);
      ps.setInt(9, user.getId());
      try (ResultSet rs = ps.executeQuery()) {
        List<UserDto> users = makeUsers(rs);
        if (users.isEmpty()) {
          MyLogger.warning("error in updateUser method of UserDAOImpl because user is empty");
          throw new NoSuchElementException();
        }
        return users.get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in updateUser method of UserDAOImpl");
      throw new MyFatalException(e);
    }
  }


  /*__Utils__*/
  @Override
  public List<UserDto> makeUsers(ResultSet rs) throws SQLException {
    List<UserDto> users = new ArrayList<>();
    UserDto user;

    while (rs.next()) {
      user = makeOneUser(rs);
      users.add(user);
    }

    return users;
  }

  @Override
  public UserDto makeOneUser(ResultSet rs) throws SQLException {
    UserDto user = userFactory.getUser();

    try {
      user.setId(rs.getInt("id_user"));
      user.setFirstname(rs.getString("firstname"));
      user.setLastname(rs.getString("lastname"));
      user.setEmail(rs.getString("email"));
      user.setPassword(rs.getString("password"));
      user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
      user.setPicture(rs.getString("picture"));
      user.setGsm(rs.getString("phone_number"));

      switch (UserRole.valueOf(rs.getString("user_role"))) {
        case USER -> user.setUserRole(UserRole.USER);
        case HELPER -> user.setUserRole(UserRole.HELPER);
        case RESPONSIBLE -> user.setUserRole(UserRole.RESPONSIBLE);
        default -> {
          MyLogger.warning("user_role in db does not correspond to any user_role in backend");
          throw new SQLException();
        }
      }
    } catch (SQLException e) {
      MyLogger.warning("user_role in db does not correspond to any user_role in backend");
      throw e;
    }

    return user;
  }

  /**
   * set every parameter for a PreparedStatement. Note that the order of parameter of the
   * PreparedStatement matter A LOT.
   *
   * @param user the user used to fill the parameter in the PreparedStatement.
   * @param ps   the PreparedStatement to which to wish to apply parameter.
   * @throws SQLException if the method is not used correctly (eg: not the same amount of param
   *                      needed, or wrong order of param).
   */
  private void setPsUserParam(UserDto user, PreparedStatement ps)
      throws SQLException {
    ps.setString(1, user.getPassword());
    ps.setString(2, user.getEmail());
    ps.setString(3, user.getFirstname());
    ps.setString(4, user.getLastname());
    ps.setTimestamp(5, Timestamp.valueOf(user.getRegistrationDate()));
    ps.setString(6, user.getUserRole().name());
    ps.setString(7, user.getPicture());
    ps.setString(8, user.getGsm());

  }
}