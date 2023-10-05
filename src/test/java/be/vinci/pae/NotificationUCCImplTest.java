package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.vinci.pae.domain.interfaces.Notification.STATE;
import be.vinci.pae.domain.interfaces.NotificationDto;
import be.vinci.pae.domain.interfaces.NotificationFactory;
import be.vinci.pae.domain.interfaces.NotificationUCC;
import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.ObjectFactory;
import be.vinci.pae.services.dao.NotificationDAO;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.TestApplicationBinder;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

/**
 * this class is used to test the implementation of NotificationUCC.
 */
class NotificationUCCImplTest {

  private static NotificationUCC notificationUCC;
  private static NotificationFactory notificationFactory;
  private static ObjectFactory objectFactory;
  private static NotificationDAO notificationDAOMock;
  private static NotificationDto expectedNotification;


  @BeforeAll
  static void beforeAll() {
    TestApplicationBinder appBinder = new TestApplicationBinder();
    ServiceLocator locator = ServiceLocatorUtilities.bind(appBinder);

    notificationDAOMock = locator.getService(NotificationDAO.class);
    notificationUCC = locator.getService(NotificationUCC.class);
    notificationFactory = locator.getService(NotificationFactory.class);
    objectFactory = locator.getService(ObjectFactory.class);
  }

  @BeforeEach
  void beforeEach() {
    expectedNotification = notificationFactory.getNotification();
    Mockito.reset(notificationDAOMock);
  }

  @Nested
  class CreateNotifForNewObjectsTests {

    @Test
    void testCreateForNewObjectsNotification() {
      // ARRANGE
      when(notificationDAOMock.createNotification(Mockito.any(NotificationDto.class))).thenReturn(
          expectedNotification);

      int validId = 1;
      ObjectDto validObject = objectFactory.getObject();
      validObject.setId(validId);
      validObject.setName("cc");
      expectedNotification.setTargetedObject(validId);

      // ACT
      NotificationDto result = notificationUCC.createNotificationForNewObject(validObject);

      // ASSERT
      assertAll(() -> assertEquals(expectedNotification, result),
          () -> assertEquals(validObject.getId(), result.getTargetedObject()));

    }

    @Test
    void testCreateNotifForNewObjectsWithFatalException() {
      // ARRANGE
      LocalDateTime date = LocalDateTime.parse("2022-05-01T10:30:00");
      expectedNotification.setId(0);
      expectedNotification.setNotificationDate(date);
      expectedNotification.setState(STATE.UNREAD);

      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(notificationDAOMock.createNotification(Mockito.any(NotificationDto.class))).thenThrow(
          exception);

      ObjectDto object = objectFactory.getObject();

      // ACT
      Executable runnable = () -> notificationUCC.createNotificationForNewObject(object);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class CreateNotifForAcceptedTests {

    @Test
    void testCreateNotifForAcceptedObjects() {
      ObjectDto validObject = objectFactory.getObject();
      validObject.setName("objet1");

      // ACT
      NotificationDto notif = notificationUCC.createNotificationForObjectAccepted(validObject);

      // ASSERT
      assertEquals("Votre objet à été " + "accepté" + " : " + validObject.getName(),
          notif.getContent());
    }

  }

  @Nested
  class CreateNotifForRefusedTests {

    @Test
    void testCreateNotifForRefusedObjects() {
      ObjectDto validObject = objectFactory.getObject();
      validObject.setName("objet1");

      // ACT
      NotificationDto notif = notificationUCC.createNotificationForObjectRefused(validObject);

      // ASSERT
      assertEquals("Votre objet à été " + "refusé" + " : " + validObject.getName(),
          notif.getContent());
    }

  }

  @Nested
  class GetAllMyNotifsTests {

    @Test
    void testgetAllMyNotifsSucces() {
      NotificationDto validNotif = notificationFactory.getNotification();
      NotificationDto validNotif2 = notificationFactory.getNotification();
      NotificationDto validNotif3 = notificationFactory.getNotification();
      List<NotificationDto> notifs = new ArrayList<>();
      notifs.add(validNotif);
      notifs.add(validNotif2);
      notifs.add(validNotif3);
      int validId = 1;
      when(notificationDAOMock.getAllMyNotifications(validId)).thenReturn(notifs);

      // ACT
      List<NotificationDto> notifsResult = notificationUCC.getAllMyNotifications(validId);

      // ASSERT
      assertAll(
          () -> assertEquals(notifs.size(), notifsResult.size()),
          () -> assertEquals(notifs, notifsResult),
          () -> verify(notificationDAOMock).getAllMyNotifications(validId)
      );
    }

    @Test
    void testgetAllMyNotifsWithFatalException() {
      List<NotificationDto> notifs = new ArrayList<>();
      NotificationDto validNotif = notificationFactory.getNotification();
      notifs.add(validNotif);
      int validId = 1;

      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(notificationDAOMock.getAllMyNotifications(validId)).thenThrow(exception);

      // ACT
      Executable runnable = () -> notificationUCC.getAllMyNotifications(validId);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }

    @Test
    void testgetAllMyNotifsNotFound() {
      List<NotificationDto> notifs = new ArrayList<>();
      int validId = 1;
      when(notificationDAOMock.getAllMyNotifications(validId)).thenReturn(notifs);

      // ACT
      Executable runnable = () -> notificationUCC.getAllMyNotifications(validId);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

  }

  @Nested
  class CreateNotifTests {

    @Test
    void testCreateNotifs() {
      ObjectDto validObject = objectFactory.getObject();
      validObject.setName("objet1");
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(notificationDAOMock.createNotification(Mockito.any(NotificationDto.class)))
          .thenThrow(exception);

      // ACT
      Executable runnable = () -> notificationUCC.createNotificationForObjectRefused(validObject);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }

    @Test
    void testCreateNotifsSecondFatal() {
      ObjectDto validObject = objectFactory.getObject();
      validObject.setName("objet1");
      when(notificationDAOMock.createNotification(Mockito.any(NotificationDto.class))).thenReturn(
          notificationFactory.getNotification());
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(notificationDAOMock.createNotificationForObjectAcceptedorRefused(
          Mockito.any(NotificationDto.class)))
          .thenThrow(exception);

      // ACT
      Executable runnable = () -> notificationUCC.createNotificationForObjectRefused(validObject);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetProposedObjectNotificationsTests {

    @Test
    void testGetProposedObjectNotificationsSucces() {
      NotificationDto validNotif = notificationFactory.getNotification();
      NotificationDto validNotif2 = notificationFactory.getNotification();
      NotificationDto validNotif3 = notificationFactory.getNotification();
      List<NotificationDto> notifs = new ArrayList<>();
      notifs.add(validNotif);
      notifs.add(validNotif2);
      notifs.add(validNotif3);
      when(notificationDAOMock.getAllNotificationProposed()).thenReturn(notifs);

      // ACT
      List<NotificationDto> notifsResult = notificationUCC.getProposedObjectNotifications();

      // ASSERT
      assertAll(
          () -> assertEquals(notifs.size(), notifsResult.size()),
          () -> assertEquals(notifs, notifsResult),
          () -> verify(notificationDAOMock).getAllNotificationProposed()
      );
    }

    @Test
    void testGetProposedObjectNotificationsWithFatalException() {
      List<NotificationDto> notifs = new ArrayList<>();
      NotificationDto validNotif = notificationFactory.getNotification();
      notifs.add(validNotif);

      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(notificationDAOMock.getAllNotificationProposed()).thenThrow(exception);

      // ACT
      Executable runnable = () -> notificationUCC.getProposedObjectNotifications();

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }

    @Test
    void testGetProposedObjectNotificationsNotFound() {
      List<NotificationDto> notifs = new ArrayList<>();
      when(notificationDAOMock.getAllNotificationProposed()).thenReturn(notifs);

      // ACT
      Executable runnable = () -> notificationUCC.getProposedObjectNotifications();

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }
  }

  @Nested
  class MarkAsReadTests {

    @Test
    void testMarkAsReadSucces() {
      NotificationDto validNotif = notificationFactory.getNotification();
      int validId = 1;
      validNotif.setId(validId);
      when(notificationDAOMock.markAsRead(validId)).thenReturn(validNotif);

      // ACT
      NotificationDto notifsResult = notificationUCC.markAsRead(validId);

      // ASSERT
      assertAll(
          () -> assertEquals(validNotif, notifsResult),
          () -> verify(notificationDAOMock).markAsRead(validId)
      );
    }

    @Test
    void testGetProposedObjectNotificationsWithFatalException() {
      NotificationDto validNotif = notificationFactory.getNotification();
      int validId = 1;
      validNotif.setId(validId);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(notificationDAOMock.markAsRead(validId)).thenThrow(exception);

      // ACT
      Executable runnable = () -> notificationUCC.markAsRead(validId);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }
}
