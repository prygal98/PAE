package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.interfaces.Notification.STATE;
import be.vinci.pae.domain.interfaces.NotificationDto;
import be.vinci.pae.domain.interfaces.NotificationFactory;
import be.vinci.pae.domain.interfaces.NotificationUCC;
import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.services.TransactionServices;
import be.vinci.pae.services.dao.NotificationDAO;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The NotificationUCC class implements the NotificationUCC interface.
 */
public class NotificationUCCImpl implements NotificationUCC {

  @Inject
  private NotificationDAO notificationDAO;

  @Inject
  private TransactionServices transaction;


  @Inject
  private NotificationFactory notificationFactory;

  @Override
  public List<NotificationDto> getProposedObjectNotifications() {

    transaction.start();

    try {
      List<NotificationDto> notifications = notificationDAO.getAllNotificationProposed();

      if (notifications.isEmpty()) {
        MyLogger.warning("The notification list is empty in getProposedObjectNotifications");
        throw new NotFoundException("The notification list is empty.");
      }

      transaction.commit();
      return notifications;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getProposedObjectNotifications");
      transaction.rollback();
      throw new MyFatalException(e);
    } catch (Exception e) {
      MyLogger.warning("Exception in method getProposedObjectNotifications: " + e.getMessage());
      transaction.rollback();
      throw e;
    }

  }

  @Override
  public NotificationDto markAsRead(int id) {

    transaction.start();

    try {
      NotificationDto notifications = notificationDAO.markAsRead(id);

      transaction.commit();
      return notifications;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method markAsRead");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method markAsRead: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public NotificationDto createNotificationForNewObject(ObjectDto objectDto) {
    NotificationDto notificationDto = notificationFactory.getNotification();
    notificationDto.setContent("Un nouvel Objet à été offert ! : " + objectDto.getName());
    notificationDto.setState(STATE.UNREAD);
    notificationDto.setTargetedObject(objectDto.getId());
    notificationDto.setNotificationDate(LocalDateTime.now());

    transaction.start();

    try {
      NotificationDto notificationDto1 = notificationDAO.createNotification(notificationDto);

      transaction.commit();
      return notificationDto1;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method createNotificationForNewObject");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method createNotificationForNewObject: " + e.getMessage());
      transaction.rollback();
      throw e;
    }
  }

  @Override
  public NotificationDto createNotificationForObjectAccepted(ObjectDto objectDto) {
    return createNotification(objectDto, "accepté");
  }

  @Override
  public NotificationDto createNotificationForObjectRefused(ObjectDto objectDto) {
    return createNotification(objectDto, "refusé");
  }

  @Override
  public NotificationDto createNotification(ObjectDto objectDto, String action) {
    NotificationDto notificationDto = notificationFactory.getNotification();
    notificationDto.setContent("Votre objet à été " + action + " : " + objectDto.getName());
    notificationDto.setState(STATE.UNREAD);
    notificationDto.setTargetedObject(objectDto.getId());
    notificationDto.setNotificationDate(LocalDateTime.now());
    transaction.start();
    NotificationDto notificationDto1;
    try {
      notificationDto1 = notificationDAO.createNotification(notificationDto);
      transaction.commit();
    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method createNotification");
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method createNotification: " + e.getMessage());
      transaction.rollback();
      throw e;
    }

    transaction.start();
    try {
      notificationDAO.createNotificationForObjectAcceptedorRefused(notificationDto1);
      transaction.commit();
    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method createNotificationForObject" + action);
      transaction.rollback();
      throw e;
    } catch (Exception e) {
      MyLogger.warning("Exception in method createNotificationForObject: " + e.getMessage());
      transaction.rollback();
      throw e;
    }

    return notificationDto;
  }

  @Override
  public List<NotificationDto> getAllMyNotifications(int id) {
    transaction.start();

    try {
      List<NotificationDto> notifications = notificationDAO.getAllMyNotifications(id);

      if (notifications.isEmpty()) {
        MyLogger.warning("The notification list is empty in getAllMyNotifications");
        throw new NotFoundException("The notification list is empty.");
      }

      transaction.commit();
      return notifications;

    } catch (MyFatalException e) {
      MyLogger.warning("MyFatalException in method getAllMyNotifications");
      transaction.rollback();
      throw new MyFatalException(e);
    } catch (Exception e) {
      MyLogger.warning("Exception in method getAllMyNotifications: " + e.getMessage());
      transaction.rollback();
      throw e;
    }

  }


}