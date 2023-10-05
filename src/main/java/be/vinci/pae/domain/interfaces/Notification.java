package be.vinci.pae.domain.interfaces;

/**
 * This interface describe Availability objects.
 */
public interface Notification extends NotificationDto {

  /**
   * Enum of the different states.
   */
  enum STATE {
    /**
     * when a notification is read.
     */
    READ,
    /**
     * when an object has been accepted.
     */
    UNREAD
  }
}
