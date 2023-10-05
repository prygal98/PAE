package be.vinci.pae.domain.interfaces;

/**
 * This interface is used to give new Notification object.
 */
public interface NotificationFactory {

  /**
   * get a NotificationDTO object.
   *
   * @return NotificationDTO.
   */
  NotificationDto getNotification();
}
