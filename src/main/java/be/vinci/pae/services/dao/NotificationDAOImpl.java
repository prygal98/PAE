package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.Notification.STATE;
import be.vinci.pae.domain.interfaces.NotificationDto;
import be.vinci.pae.domain.interfaces.NotificationFactory;
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
 * This class provides access to notification data in the database.
 */
public class NotificationDAOImpl implements NotificationDAO {

  @Inject
  private NotificationFactory notificationFactory;

  @Inject
  private ObjectDAO objectDAO;

  @Inject
  private DalServices dal;

  /* CREATE */
  @Override
  public NotificationDto createNotification(NotificationDto notificationDto) {
    // QUERY
    String sql = "INSERT INTO project.notifications VALUES (DEFAULT, ?, ?, ?, ?) RETURNING * ;";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sql)) {
      setPsNotificationParam(notificationDto, ps);
      try (ResultSet rs = ps.executeQuery()) {
        return makeNotificationsList(rs).get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("Request failed in createNotificationForNewObject");
      throw new MyFatalException(e);
    }
  }

  @Override
  public boolean createNotificationForObjectAcceptedorRefused(
      NotificationDto notificationDto) {
    String sql = "INSERT INTO project.notifications_users (user_id, notification) "
        + "VALUES (?, ?) RETURNING * ;";

    try (PreparedStatement ps = dal.getPs(sql)) {
      int idUser = objectDAO.getOneObjectById(notificationDto.getTargetedObject()).getOwnerId();
      ps.setInt(1, idUser); // get id user from the object id by calling objectDAO
      ps.setInt(2, notificationDto.getId());
      try (ResultSet rs = ps.executeQuery()) {
        return true;
      }

    } catch (SQLException e) {
      MyLogger.warning("error in createNotificationForObjectAccepted method "
          + "of NotificationDAOImpl");
      throw new MyFatalException(e);
    }


  }

  @Override
  public List<NotificationDto> getAllMyNotifications(int id) {

    // QUERY
    String sqlGet = "SELECT * from project.notifications_users nu , project.notifications n "
        + "where n.id_notification = nu.notification and nu.user_id = ? ;";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlGet)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        return makeNotificationsList(rs);
      }
    } catch (SQLException e) {
      MyLogger.warning("Request failed to get all the notifications");
      throw new MyFatalException(e);
    }
  }


  /* READ */
  @Override
  public List<NotificationDto> getAllNotificationProposed() {
    // QUERY
    String sqlGet = "SELECT * "
        + "FROM project.notifications n "
        + "INNER JOIN project.objects o ON n.targeted_object = o.id_object "
        + "WHERE o.state = 'OFFERED' ;";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlGet)) {
      try (ResultSet rs = ps.executeQuery()) {
        return makeNotificationsList(rs);
      }

    } catch (SQLException e) {
      MyLogger.warning("Request failed to get all the notifications");
      throw new MyFatalException(e);
    }
  }

  /* UPDATE */
  @Override
  public NotificationDto markAsRead(int id) {
    // QUERY
    String sqlUpdate = " UPDATE project.notifications set "
        + "state = 'READ' WHERE id_notification = ? RETURNING * ";

    // EXECUTE QUERY
    try (PreparedStatement ps = dal.getPs(sqlUpdate)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        return makeNotificationsList(rs).get(0);
      }

    } catch (SQLException e) {
      MyLogger.warning("error in markAsRead method of NotificationDAOImpl");
      throw new MyFatalException(e);
    }
  }

  /* Utils */
  @Override
  public List<NotificationDto> makeNotificationsList(ResultSet rs) throws SQLException {
    List<NotificationDto> notificationList = new ArrayList<>();
    NotificationDto notification;

    try {
      while (rs.next()) {
        notification = makeOneNotification(rs);
        notificationList.add(notification);
      }
    } catch (SQLException e) {
      MyLogger.warning("One of the column name is incorrect in"
          + " notificationDAO -> makeNotification()");
      throw e;

    }

    return notificationList;
  }

  @Override
  public NotificationDto makeOneNotification(ResultSet rs) throws SQLException {
    NotificationDto notification = notificationFactory.getNotification();

    try {
      notification.setId(rs.getInt("id_notification"));
      notification.setContent(rs.getString("content"));
      notification.setTargetedObject(rs.getInt("targeted_object"));
      if (rs.getDate("notification_date") != null) {
        notification.setNotificationDate(rs.getTimestamp("notification_date").toLocalDateTime());
      }

      switch (STATE.valueOf(rs.getString("state"))) {
        case READ -> notification.setState(STATE.READ);
        case UNREAD -> notification.setState(STATE.UNREAD);
        default -> {
          MyLogger.warning("state in db does not correspond to any state in backend");
        }
      }

    } catch (SQLException e) {
      MyLogger.warning("One of the column name is incorrect "
          + "in notificationDAO -> makeNotification()");
      throw e;
    }

    return notification;
  }

  /**
   * set every parameter for a PreparedStatement. Note that the order of parameter of the
   * PreparedStatement matter A LOT.
   *
   * @param notification the notification used to fill the parameter in the PreparedStatement.
   * @param ps           the PreparedStatement to which to wish to apply parameter.
   * @throws SQLException if the method is not used correctly (eg: not the same amount of param
   *                      needed, or wrong order of param).
   */
  private void setPsNotificationParam(NotificationDto notification, PreparedStatement ps)
      throws SQLException {

    ps.setString(1, notification.getContent());
    ps.setString(2, notification.getState().name());
    ps.setInt(3, notification.getTargetedObject());
    if (notification.getNotificationDate() != null) {
      ps.setTimestamp(4, Timestamp.valueOf(notification.getNotificationDate()));
    } else {
      ps.setTimestamp(4, null);
    }

  }

}