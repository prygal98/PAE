package be.vinci.pae.services.dao;

import be.vinci.pae.domain.interfaces.NotificationDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface describe the data access notification.
 */
public interface NotificationDAO {

  /**
   * Retrieves a list of all notifications for objects proposed in the database.
   *
   * @return a list of notificationdto representing all notifications in the database.
   */
  List<NotificationDto> getAllNotificationProposed();

  /**
   * Create a list of notifcationsdto using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return a list of notificationDTO.
   * @throws SQLException sql exception
   */
  List<NotificationDto> makeNotificationsList(ResultSet rs) throws SQLException;

  /**
   * Create a single notificationDTO using the ResultSet of a query.
   *
   * @param rs : the resultSet returned by a query to the database.
   * @return one notificationDTO.
   * @throws SQLException sql exception
   */
  NotificationDto makeOneNotification(ResultSet rs) throws SQLException;

  /**
   * Marks the notification with the specified ID as read.
   *
   * @param id the ID of the notification to mark as read
   * @return the NotificationDto object representing the notification marked as read
   */
  NotificationDto markAsRead(int id);

  /**
   * Create a notifcation for an object.
   *
   * @param notificationDto the notificationDto object representing the new notification
   * @return Notification DTO from created notif
   */
  NotificationDto createNotification(NotificationDto notificationDto);

  /**
   * Create a notifcation for an object that has been accepted or refused.
   *
   * @param notificationDto the notificationDto object representing the new notification
   * @return true if done
   */
  boolean createNotificationForObjectAcceptedorRefused(NotificationDto notificationDto);

  /**
   * Retrieves a list of all my notifications.
   *
   * @param id of user owner of notifs
   * @return a list of notificationdto representing all notifications in the database.
   */
  List<NotificationDto> getAllMyNotifications(int id);
}
