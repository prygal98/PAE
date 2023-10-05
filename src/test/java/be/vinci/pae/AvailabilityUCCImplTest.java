package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.vinci.pae.domain.interfaces.AvailabilityDto;
import be.vinci.pae.domain.interfaces.AvailabilityFactory;
import be.vinci.pae.domain.interfaces.AvailabilityUCC;
import be.vinci.pae.services.dao.AvailabilityDAO;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

/**
 * this class is used to test the implementation of AvailabilityUCC.
 */
class AvailabilityUCCImplTest {

  private static AvailabilityUCC availabilityUCC;
  private static AvailabilityFactory availabilityFactory;
  private static AvailabilityDAO availabilityDAOMock;
  private AvailabilityDto expectedAvailability;

  @BeforeAll
  static void beforeAll() {
    TestApplicationBinder appBinder = new TestApplicationBinder();
    ServiceLocator locator = ServiceLocatorUtilities.bind(appBinder);

    availabilityDAOMock = locator.getService(AvailabilityDAO.class);
    availabilityUCC = locator.getService(AvailabilityUCC.class);
    availabilityFactory = locator.getService(AvailabilityFactory.class);
  }

  @BeforeEach
  void beforeEach() {
    expectedAvailability = availabilityFactory.getAvailability();
    Mockito.reset(availabilityDAOMock);
  }

  @Nested
  class AddAvailabilityTests {

    @DisplayName("Test to create availability succes")
    @Test
    void addAvailabilitySucces() {
      // ARRANGE
      LocalDateTime date = LocalDateTime.parse("2022-05-01T10:30:00");
      String timeSlot = "matin";
      expectedAvailability.setId(0);
      expectedAvailability.setAvailability(date);
      expectedAvailability.setTimeSlot(timeSlot);

      Mockito.when(availabilityDAOMock.createOneAvailability(expectedAvailability))
          .thenReturn(expectedAvailability);

      // ACT
      AvailabilityDto availability = availabilityUCC.addAvailability(expectedAvailability);

      // ASSERT
      assertEquals(expectedAvailability, availability);
    }

    @DisplayName("Test to create availability with fatal exception")
    @Test
    void addAvailabilityWithFatalException() {
      // ARRANGE
      LocalDateTime date = LocalDateTime.parse("2022-05-01T10:30:00");
      String timeSlot = "matin";
      expectedAvailability.setId(0);
      expectedAvailability.setAvailability(date);
      expectedAvailability.setTimeSlot(timeSlot);

      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(availabilityDAOMock.createOneAvailability(expectedAvailability))
          .thenThrow(exception);

      // ACT
      Executable runnable = () -> availabilityUCC.addAvailability(expectedAvailability);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }

    @DisplayName("Test to create availability with exception")
    @Test
    void addAvailabilityWithException() {
      // ARRANGE
      LocalDateTime date = LocalDateTime.parse("2022-05-01T10:30:00");
      String timeSlot = "matin";
      expectedAvailability.setId(0);
      expectedAvailability.setAvailability(date);
      expectedAvailability.setTimeSlot(timeSlot);

      when(availabilityDAOMock.createOneAvailability(expectedAvailability))
          .thenThrow(RuntimeException.class);

      // ACT
      Executable runnable = () -> availabilityUCC.addAvailability(expectedAvailability);

      // ASSERT
      assertThrows(Exception.class, runnable);
    }
  }

  @Nested
  class GetAllAvailabilitiesTests {

    @DisplayName("testing that all the availabilities are returned when getAllAvailabilities ")
    @Test
    void testGetAllAvailabilities() {
      // ARRANGE
      List<AvailabilityDto> availabilityDtos = new ArrayList<>();
      AvailabilityDto availability1 = availabilityFactory.getAvailability();
      AvailabilityDto availability2 = availabilityFactory.getAvailability();
      availabilityDtos.add(availability1);
      availabilityDtos.add(availability2);

      when(availabilityDAOMock.getAllAvailabilities()).thenReturn(availabilityDtos);

      // ACT
      List<AvailabilityDto> result = availabilityUCC.getAllAvailabilities();

      // ASSERT
      assertEquals(availabilityDtos.size(), result.size());
      assertEquals(availabilityDtos, result);
      verify(availabilityDAOMock).getAllAvailabilities();
    }

    @DisplayName("testing that all the availabilities are returned getAllAvailabilities "
        + "If the returned objects list is empty, should throw an error")
    @Test
    void testGetAllObjectsWithException() {
      // ARRANGE
      List<AvailabilityDto> availabilities = new ArrayList<>();
      when(availabilityDAOMock.getAllAvailabilities()).thenReturn(availabilities);

      // ACT
      Executable runnable = () -> availabilityUCC.getAllAvailabilities();

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testAllObjectWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(availabilityDAOMock.getAllAvailabilities()).thenThrow(exception);

      // ACT
      Executable runnable = () -> availabilityUCC.getAllAvailabilities();

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }
}
