package be.vinci.pae.domain.interfaces;

import java.util.List;

/**
 * This interface describe the UCC Class for notifications.
 */
public interface NotificationUCC {

  /**
   * Get a list of all the Proposed Objects.
   *
   * @return a List of all notifications of objects proposed
   */
  List<NotificationDto> getProposedObjectNotifications();


  /**
   * Mark a notification as read.
   *
   * @param id id of the targeted object
   * @return the notification with the read state
   */
  NotificationDto markAsRead(int id);

  /**
   * Create a notification for an object that has been offered.
   *
   * @param object targeted object
   * @return a notification about an object
   */
  NotificationDto createNotificationForNewObject(ObjectDto object);

  /**
   * Create a notification for an object that has been accepted.
   *
   * @param objectDto id of the targeted object
   * @return a notification about an object
   */
  NotificationDto createNotificationForObjectAccepted(ObjectDto objectDto);

  /**
   * Create a notification for an object that has been refused.
   *
   * @param objectDto id of the targeted object
   * @return a notification about an object
   */
  NotificationDto createNotificationForObjectRefused(ObjectDto objectDto);

  /**
   * Create a notification for an object that has been accepted or refused. Method used by
   * createNotificationForObjectAccepted and createNotificationForObjectRefused.
   *
   * @param objectDto id of the targeted object.
   * @param action    the message display in the notification.
   * @return a notification about an object
   */
  NotificationDto createNotification(ObjectDto objectDto, String action);

  /**
   * Get a list of all my notifications.
   *
   * @param id of user owner of notifs
   * @return a List of all notifications of objects proposed
   */
  List<NotificationDto> getAllMyNotifications(int id);
}