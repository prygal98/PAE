package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.NotificationDto;
import be.vinci.pae.domain.interfaces.NotificationFactory;

/**
 * This class is used to create Notification objects.
 */
public class NotificationFactoryImpl implements NotificationFactory {

  @Override
  public NotificationDto getNotification() {
    return new NotificationImpl();
  }

}
