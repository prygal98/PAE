package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.Notification.STATE;
import be.vinci.pae.domain.interfaces.NotificationDto;
import java.time.LocalDateTime;

/**
 * Represents a notification entity in the application.
 */
public class NotificationImpl implements NotificationDto {

  private int id;
  private String content;
  private STATE state;
  private int targetedObject;
  private LocalDateTime notificationDate;

  /**
   * Constructor for NotificationImpl.
   */
  public NotificationImpl() {
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public STATE getState() {
    return state;
  }

  @Override
  public void setState(STATE state) {
    this.state = state;
  }

  @Override
  public int getTargetedObject() {
    return targetedObject;
  }

  @Override
  public void setTargetedObject(int targetedObject) {
    this.targetedObject = targetedObject;
  }

  @Override
  public LocalDateTime getNotificationDate() {
    return notificationDate;
  }

  @Override
  public void setNotificationDate(LocalDateTime notificationDate) {
    this.notificationDate = notificationDate;
  }
}
