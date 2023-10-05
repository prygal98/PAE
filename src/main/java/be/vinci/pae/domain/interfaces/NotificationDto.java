package be.vinci.pae.domain.interfaces;

import be.vinci.pae.domain.interfaces.Notification.STATE;
import java.time.LocalDateTime;

/**
 * This interface represents a data transfer object for a notification. It defines the methods that
 * can be used to access and manipulate the notification data.
 */
public interface NotificationDto {

  /**
   * Returns the unique identifier of this notification.
   *
   * @return the notification's ID.
   */
  int getId();

  /**
   * Sets the unique identifier of this notification.
   *
   * @param id the new ID to set.
   */
  void setId(int id);

  /**
   * Returns the content of this notification.
   *
   * @return the notification's content.
   */
  String getContent();

  /**
   * Sets the content of this notification.
   *
   * @param content the new content to set.
   */
  void setContent(String content);

  /**
   * Returns the state of this notification, whether it has been read or not.
   *
   * @return the notification's state.
   */
  STATE getState();

  /**
   * Sets the state of this notification, whether it has been read or not.
   *
   * @param state the new state to set.
   */
  void setState(STATE state);

  /**
   * Returns the ID of the object that this notification is related to.
   *
   * @return the ID of the targeted object.
   */
  int getTargetedObject();

  /**
   * Sets the ID of the object that this notification is related to.
   *
   * @param targetedObject the ID of the targeted object.
   */
  void setTargetedObject(int targetedObject);

  /**
   * Returns the date and time when this notification was created.
   *
   * @return the notification's creation date and time.
   */
  LocalDateTime getNotificationDate();

  /**
   * Sets the date and time when this notification was created.
   *
   * @param notificationDate the new creation date and time to set.
   */
  void setNotificationDate(LocalDateTime notificationDate);
}
