package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import be.vinci.pae.domain.interfaces.Object.STATE;
import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.ObjectFactory;
import be.vinci.pae.domain.interfaces.ObjectUCC;
import be.vinci.pae.domain.interfaces.TypeObjectDto;
import be.vinci.pae.domain.interfaces.TypeObjectFactory;
import be.vinci.pae.services.dao.ObjectDAO;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.TestApplicationBinder;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
 * This class is used to test the implementation of ApplicationBinder.
 */
public class ObjectsUCCImplTest {

  private static ObjectDAO objectDAOMock;
  private static ObjectUCC objectUCC;
  private static ObjectFactory objectFactory;
  private static TypeObjectFactory typeObjectFactory;
  private static int id = 1;
  private ObjectDto expectedObject;

  @BeforeAll
  static void beforeAll() {
    TestApplicationBinder appBinder = new TestApplicationBinder();

    ServiceLocator locator = ServiceLocatorUtilities.bind(appBinder);
    objectFactory = locator.getService(ObjectFactory.class);
    typeObjectFactory = locator.getService(TypeObjectFactory.class);
    objectUCC = locator.getService(ObjectUCC.class);
    objectDAOMock = locator.getService(ObjectDAO.class);
  }

  @BeforeEach
  void beforeEach() {
    expectedObject = objectFactory.getObject();
    expectedObject.setId(++id);
    Mockito.reset(objectDAOMock);
  }

  @Nested
  class AcceptTests {

    @DisplayName("testing that the method accept the object proposition without any error")
    @Test
    void testAcceptObjectPropositionAllowed() {
      // ARRANGE
      expectedObject.setState(STATE.OFFERED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);

      // ACT
      ObjectDto updatedObjectDto = objectUCC.acceptObjectProposition(id);

      // ASSERT
      assertEquals(STATE.CONFIRMED, updatedObjectDto.getState());
    }

    @DisplayName("testing that the method accept the object proposition with a not allowed state."
        + "this method should throw an error : Not allowedState")
    @Test
    void testAcceptObjectPropositionNotAllowedState() {
      // ARRANGE
      expectedObject.setState(STATE.CONFIRMED);
      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);

      // ACT
      Executable runnable = () -> objectUCC.acceptObjectProposition(id);

      // ASSERT
      assertThrows(NotAllowedException.class, runnable);
    }

    @DisplayName("testing that the method accept the object proposition. "
        + "This test try to find the object in the database"
        + " and throw an error if it is not present")
    @Test
    void testAcceptObjectPropositionWithExceptionThrown() {
      // ARRANGE
      expectedObject.setState(STATE.CONFIRMED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);
      // TODO renvoyer la bonne erreur en testant la db sans aucun objet

      // ACT
      Executable runnable = () -> objectUCC.acceptObjectProposition(id);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testAcceptObjectWithException() {
      ObjectDto objectDto = objectFactory.getObject();
      objectDto.setId(2);
      objectDto.setState(STATE.OFFERED);
      when(objectDAOMock.getOneObjectById(objectDto.getId())).thenReturn(objectDto);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(objectDto)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.acceptObjectProposition(objectDto.getId());

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class DenyTests {

    @DisplayName("testing that the method change the object state to deny without any error")
    @Test
    void testDenyObjectStateAllowed() {
      // ARRANGE
      String refusalReason = "not interesting object";
      expectedObject.setState(STATE.OFFERED);
      expectedObject.setRefusalReason(refusalReason);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);

      // ACT
      ObjectDto updatedObjectDto = objectUCC.denyObjectState(id, refusalReason);

      // ASSERT
      assertEquals(STATE.DENIED, updatedObjectDto.getState());
      assertEquals(refusalReason, updatedObjectDto.getRefusalReason());
    }

    @DisplayName("testing that the method deny the object proposition"
        + "should throw a not allowedException if the initial object state is not OFFERED ")
    @Test
    void testDenyObjectStateNotAllowedState() {
      // ARRANGE
      String refusalReason = "not interesting object";
      expectedObject.setState(STATE.SOLD);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);

      // ACT
      Executable runnable = () -> objectUCC.denyObjectState(id, refusalReason);

      // ASSERT
      assertThrows(NotAllowedException.class, runnable);
    }

    @DisplayName("testing that the method deny the object proposition "
        + "this test try to find the object in the database"
        + " and throw an error if it is not present")
    @Test
    void testDenyObjectStateWithExceptionThrown() {
      // ARRANGE
      String refusalReason = "not interesting object";
      expectedObject.setState(STATE.OFFERED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);

      // ACT
      Executable runnable = () -> objectUCC.denyObjectState(id, refusalReason);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testDenyWithFatalException() {
      ObjectDto validObject = objectFactory.getObject();
      int validId = 3;
      validObject.setState(STATE.OFFERED);
      when(objectDAOMock.getOneObjectById(validId)).thenReturn(validObject);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(validObject)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.denyObjectState(validId, "test");

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class WorkshopTests {

    @DisplayName("this test try to change the object state from CONFIRMED to WORKSHOP state")
    @Test
    void testWorkshopObjectStateAllowed() {
      // ARRANGE
      expectedObject.setState(STATE.CONFIRMED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);

      // ACT
      ObjectDto updatedObjectDto = objectUCC.workshopObjectState(id);

      // ASSERT
      assertEquals(STATE.WORKSHOP, updatedObjectDto.getState());
    }

    @DisplayName("this test try to change the object state from CONFIRMED to WORKSHOP state"
        + "if the initial state of the object is different from CONFIRMED, "
        + "it should throw a NotAllowedException")
    @Test
    void testWorkshopObjectStateNotAllowedState() {
      // ARRANGE
      expectedObject.setState(STATE.DENIED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);

      // ACT
      Executable runnable = () -> objectUCC.workshopObjectState(id);

      // ASSERT
      assertThrows(NotAllowedException.class, runnable);
    }

    @DisplayName("testing that the method put the object to workshop state "
        + "this test try to find the object in the database"
        + " and throw an error if it is not present")
    @Test
    void testWorkshopObjectStateWithExceptionThrown() {
      // ARRANGE
      expectedObject.setState(STATE.CONFIRMED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);
      // TODO renvoyer la bonne erreur en testant la db sans aucun objet

      // ACT
      Executable runnable = () -> objectUCC.workshopObjectState(id);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testWorkshopObjectWithFatalException() {
      ObjectDto objectDto = objectFactory.getObject();
      objectDto.setId(2);
      objectDto.setState(STATE.CONFIRMED);
      when(objectDAOMock.getOneObjectById(objectDto.getId())).thenReturn(objectDto);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(objectDto)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.workshopObjectState(2);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class StoreTests {

    @DisplayName("this test try to change the object state from Workshop to Store state")
    @Test
    void testStoreObjectStateWithWorkshopState() {
      // ARRANGE
      expectedObject.setState(STATE.WORKSHOP);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);

      // ACT
      ObjectDto updatedObjectDto = objectUCC.storeObjectState(id);

      // ASSERT
      assertEquals(STATE.STORE, updatedObjectDto.getState());
    }

    @DisplayName("this test try to change the object state from CONFIRMED to Store state")
    @Test
    void testStoreObjectStateWithConfirmState() {
      // ARRANGE
      expectedObject.setState(STATE.CONFIRMED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);

      // ACT
      ObjectDto updatedObjectDto = objectUCC.storeObjectState(id);

      // ASSERT
      assertEquals(STATE.STORE, updatedObjectDto.getState());
    }

    @DisplayName("this test try to change the object state to Store state"
        + "If the initial state is different from WORKSHOP or CONFIRMED "
        + "it should throw a NotAllowedException")
    @Test
    void testStoreObjectStateNotAllowedState() {
      // ARRANGE
      expectedObject.setState(STATE.DENIED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);

      // ACT
      Executable runnable = () -> objectUCC.storeObjectState(id);

      // ASSERT
      assertThrows(NotAllowedException.class, runnable);
    }

    @DisplayName("testing that the method set the object state to Store  "
        + "this test try to find the object in the database"
        + " and throw an error if it is not present")
    @Test
    void testStoreObjectStateWithExceptionThrown() {
      // ARRANGE
      expectedObject.setState(STATE.OFFERED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);
      // TODO renvoyer la bonne erreur en testant la db sans aucun objet

      // ACT
      Executable runnable = () -> objectUCC.storeObjectState(id);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testStoreObjectWithFatalException() {
      ObjectDto objectDto = objectFactory.getObject();
      objectDto.setId(2);
      objectDto.setState(STATE.CONFIRMED);
      when(objectDAOMock.getOneObjectById(objectDto.getId())).thenReturn(objectDto);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(objectDto)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.storeObjectState(2);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class SaleTests {

    @DisplayName("this test try to change the object state to SALE ")
    @Test
    void testSaleObjectStateAllowed() {
      // ARRANGE

      expectedObject.setState(STATE.STORE);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);
      int sellingPrice = 30;
      // ACT
      ObjectDto updatedObjectDto = objectUCC.saleObjectState(id, sellingPrice);

      // ASSERT
      assertEquals(STATE.SALE, updatedObjectDto.getState());
      assertEquals(sellingPrice, updatedObjectDto.getSellingPrice());
    }

    @DisplayName("this test try to change the object state to SALE "
        + "If the initial state is different from Store state"
        + "should throw a NotAllowedException")
    @Test
    void testSaleObjectStateNotAllowedState() {
      // ARRANGE
      expectedObject.setState(STATE.CONFIRMED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);

      // ACT
      Executable runnable = () -> objectUCC.saleObjectState(id, 30);

      // ASSERT
      assertThrows(NotAllowedException.class, runnable);
    }

    @DisplayName("testing that the method set the object state to SALE  "
        + "this test try to find the object in the database"
        + " and throw an error if it is not present")
    @Test
    void testSaleObjectStateWithExceptionThrown() {
      // ARRANGE
      expectedObject.setState(STATE.STORE);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);
      // TODO renvoyer la bonne erreur en testant la db sans aucun objet

      // ACT
      Executable runnable = () -> objectUCC.saleObjectState(id, 30);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testSaleObjectWithFatalException() {
      ObjectDto objectDto = objectFactory.getObject();
      objectDto.setId(2);
      objectDto.setState(STATE.STORE);
      when(objectDAOMock.getOneObjectById(objectDto.getId())).thenReturn(objectDto);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(objectDto)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.saleObjectState(2, 10);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class SoldTests {

    @DisplayName("this test try to change the object state to SOLD")
    @Test
    void testSoldObjectStateAllowed() {
      // ARRANGE
      expectedObject.setState(STATE.SALE);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);

      // ACT
      ObjectDto updatedObjectDto = objectUCC.soldObjectState(id);

      // ASSERT
      assertEquals(STATE.SOLD, updatedObjectDto.getState());

    }

    @DisplayName("this test try to change the object state to SOLD"
        + "If the initial state is different from SALE"
        + "it should throw a NotAllowedException")
    @Test
    void testSoldObjectStateNotAllowedState() {
      // ARRANGE
      expectedObject.setState(STATE.CONFIRMED);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);

      // ACT
      Executable runnable = () -> objectUCC.soldObjectState(id);

      // ASSERT
      assertThrows(NotAllowedException.class, runnable);
    }

    @DisplayName("testing that the method set the object state to SOLD  "
        + "this test try to find the object in the database"
        + " and throw an error if it is not present")
    @Test
    void testSoldObjectStateWithExceptionThrown() {
      // ARRANGE
      expectedObject.setState(STATE.SALE);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);
      // TODO renvoyer la bonne erreur en testant la db sans aucun objet

      // ACT
      Executable runnable = () -> objectUCC.soldObjectState(id);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testSoldObjectWithFatalException() {
      ObjectDto objectDto = objectFactory.getObject();
      objectDto.setId(2);
      objectDto.setState(STATE.SALE);
      when(objectDAOMock.getOneObjectById(objectDto.getId())).thenReturn(objectDto);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(objectDto)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.soldObjectState(2);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class WithdrawnTests {

    @DisplayName("this test try to change the object state to WITHDRAWN")
    @Test
    void testWithdrawnObjectStateAllowed() {
      // ARRANGE
      expectedObject.setState(STATE.SALE);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);
      when(objectDAOMock.updateOneObject(expectedObject)).thenReturn(expectedObject);

      // ACT
      ObjectDto updatedObjectDto = objectUCC.withdrawnObjectState(id);

      // ASSERT
      assertEquals(STATE.WITHDRAWN, updatedObjectDto.getState());
    }

    @DisplayName("testing that the method set the object state to WITHDRAWN "
        + "this test try to find the object in the database"
        + " and throw an error if it is not present")
    @Test
    void testWithdrawnObjectStateWithExceptionThrown() {
      // ARRANGE
      expectedObject.setState(STATE.SALE);

      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);
      // TODO renvoyer la bonne erreur en testant la db sans aucun objet

      // ACT
      Executable runnable = () -> objectUCC.withdrawnObjectState(id);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testWithdrawnObjectWithFatalException() {
      ObjectDto objectDto = objectFactory.getObject();
      objectDto.setId(2);
      objectDto.setState(STATE.SALE);
      when(objectDAOMock.getOneObjectById(objectDto.getId())).thenReturn(objectDto);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(objectDto)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.withdrawnObjectState(2);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetOneObjectTests {

    @DisplayName("this test try to get one object by id "
        + "if the object is present in the db, should return the object")
    @Test
    void testGetOneObjectById() {
      // ARRANGE
      when(objectDAOMock.getOneObjectById(id)).thenReturn(expectedObject);

      // ACT
      ObjectDto result = objectUCC.getOneObjectById(id);

      // ASSERT
      assertEquals(expectedObject.getId(), result.getId());
    }

    @DisplayName("this test try to get one object by id "
        + "if the object is not present in the db, should throw a notFoundException")
    @Test
    void testGetOneObjectByIdNotFoundInDb() {
      // ARRANGE
      when(objectDAOMock.getOneObjectById(id)).thenReturn(null);
      // TODO renvoyer la bonne erreur en testant la db sans aucun objet

      // ACT
      Executable runnable = () -> objectUCC.getOneObjectById(id);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetOneObjectByIdWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      int id = 1;
      when(objectDAOMock.getOneObjectById(id)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getOneObjectById(id);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetAllObjectsTests {

    @DisplayName("testing that all the objects are returned when trying to getAllTheObjects ")
    @Test
    void testGetAllObjects() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();
      ObjectDto object1 = objectFactory.getObject();
      ObjectDto object2 = objectFactory.getObject();
      objects.add(object1);
      objects.add(object2);

      when(objectDAOMock.getAllObjects()).thenReturn(objects);

      // ACT
      List<ObjectDto> result = objectUCC.getAllObjects();

      // ASSERT
      assertEquals(objects.size(), result.size());
    }

    @DisplayName("testing that all the objects are returned when trying to getAllTheObjects "
        + "If the returned objects list is empty, should throw an error")
    @Test
    void testGetAllObjectsWithException() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();
      when(objectDAOMock.getAllObjects()).thenReturn(objects);

      // ACT
      Executable runnable = () -> objectUCC.getAllObjects();

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testAllObjectWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.getAllObjects()).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getAllObjects();

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetAllTypesObjectsTests {

    @DisplayName("testing that all the objects are returned when trying to getAllTypeObjects ")
    @Test
    void testGetAllTypesObjects() {
      // ARRANGE
      List<TypeObjectDto> types = new ArrayList<>();
      TypeObjectDto type1 = typeObjectFactory.getType();
      TypeObjectDto type2 = typeObjectFactory.getType();
      types.add(type1);
      types.add(type2);

      when(objectDAOMock.getAllTypesObject()).thenReturn(types);

      // ACT
      List<TypeObjectDto> result = objectUCC.getAllTypeObjects();

      // ASSERT
      assertEquals(types.size(), result.size());
    }

    @DisplayName("testing that all the objects are returned when trying to getAllTheObjects "
        + "If the returned objects list is empty, should throw an error")
    @Test
    void testGetAllObjectsTypesWithException() {
      // ARRANGE
      List<TypeObjectDto> types = new ArrayList<>();
      when(objectDAOMock.getAllTypesObject()).thenReturn(types);

      // ACT
      Executable runnable = () -> objectUCC.getAllTypeObjects();

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testAllTypesObjectWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.getAllTypesObject()).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getAllTypeObjects();

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetNumberObjectsByPeriodTests {

    @DisplayName("testing that all the objects are returned ")
    @Test
    void testGetObjectsByPeriodSuccess() {
      // ARRANGE
      Map<Integer, Integer> objectsByPeriod = new HashMap<>();

      objectsByPeriod.put(2009, 3);
      objectsByPeriod.put(2010, 6);
      objectsByPeriod.put(2015, 2);

      when(objectDAOMock.getNumberOfProposedObjectsByPeriod()).thenReturn(objectsByPeriod);

      // ACT
      Map<Integer, Integer> result = objectUCC.getNumberOfProposedObjectsByPeriod();

      // ASSERT
      assertEquals(objectsByPeriod.size(), result.size());
      assertEquals(objectsByPeriod, result);
      Mockito.verify(objectDAOMock).getNumberOfProposedObjectsByPeriod();
    }

    @DisplayName("the returned objects list is empty, should throw an error")
    @Test
    void testGetObjectsByPeriodNotFound() {
      // ARRANGE
      Map<Integer, Integer> objectsByPeriod = new HashMap<>();
      when(objectDAOMock.getNumberOfProposedObjectsByPeriod()).thenReturn(objectsByPeriod);

      // ACT
      Executable runnable = () -> objectUCC.getNumberOfProposedObjectsByPeriod();

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetObjectsByPeriodWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.getNumberOfProposedObjectsByPeriod()).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getNumberOfProposedObjectsByPeriod();

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetObjectsBetweenTwoPricesTests {

    @DisplayName("testing that all the objects are returned")
    @Test
    void testGetObjectsBetweenTwoPricesSuccess() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();

      ObjectDto validObject1 = objectFactory.getObject();
      ObjectDto validObject2 = objectFactory.getObject();
      ObjectDto validObject3 = objectFactory.getObject();

      objects.add(validObject1);
      objects.add(validObject2);
      objects.add(validObject3);

      when(objectDAOMock.getObjectsBetweenTwoPrice(10, 15)).thenReturn(objects);

      // ACT
      List<ObjectDto> result = objectUCC.getObjectsBetweenTwoPrice(10, 15);

      // ASSERT
      assertEquals(objects.size(), result.size());
      assertEquals(objects, result);
      Mockito.verify(objectDAOMock).getObjectsBetweenTwoPrice(10, 15);
    }

    @DisplayName("the returned objects list is empty, should throw an error")
    @Test
    void testGetObjectsBetweenTwoPricesNotFound() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();
      when(objectDAOMock.getObjectsBetweenTwoPrice(10, 15)).thenReturn(objects);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsBetweenTwoPrice(10, 15);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetObjectsBetweenTwoPricesWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.getObjectsBetweenTwoPrice(10, 15)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsBetweenTwoPrice(10, 15);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetObjectsBetweenTwoDatesTests {

    @DisplayName("testing that all the objects are returned")
    @Test
    void testGetObjectsBetweenTwoDatesSuccess() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();

      ObjectDto validObject1 = objectFactory.getObject();
      ObjectDto validObject2 = objectFactory.getObject();
      ObjectDto validObject3 = objectFactory.getObject();

      objects.add(validObject1);
      objects.add(validObject2);
      objects.add(validObject3);

      LocalDateTime dateStart = LocalDateTime.parse("2022-05-01T10:30:00");
      LocalDateTime dateEnd = LocalDateTime.parse("2022-06-01T10:30:00");
      when(objectDAOMock.getObjectsBetweenTwoDate(dateStart, dateEnd)).thenReturn(objects);

      // ACT
      List<ObjectDto> result = objectUCC.getObjectsBetweenTwoDate(dateStart, dateEnd);

      // ASSERT
      assertEquals(objects.size(), result.size());
      assertEquals(objects, result);
      Mockito.verify(objectDAOMock).getObjectsBetweenTwoDate(dateStart, dateEnd);
    }

    @DisplayName("the returned objects list is empty, should throw an error")
    @Test
    void testGetObjectsBetweenTwoDatesNotFound() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();
      LocalDateTime dateStart = LocalDateTime.parse("2022-05-01T10:30:00");
      LocalDateTime dateEnd = LocalDateTime.parse("2022-06-01T10:30:00");
      when(objectDAOMock.getObjectsBetweenTwoDate(dateStart, dateEnd)).thenReturn(objects);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsBetweenTwoDate(dateStart, dateEnd);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetObjectsBetweenTwoPricesWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      LocalDateTime dateStart = LocalDateTime.parse("2022-05-01T10:30:00");
      LocalDateTime dateEnd = LocalDateTime.parse("2022-06-01T10:30:00");
      when(objectDAOMock.getObjectsBetweenTwoDate(dateStart, dateEnd)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsBetweenTwoDate(dateStart, dateEnd);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetObjectsSortedByTypesTests {

    @DisplayName("testing that all the objects are returned")
    @Test
    void testGetObjectsSortedByTypesSuccess() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();

      ObjectDto validObject1 = objectFactory.getObject();
      ObjectDto validObject2 = objectFactory.getObject();
      ObjectDto validObject3 = objectFactory.getObject();

      objects.add(validObject1);
      objects.add(validObject2);
      objects.add(validObject3);

      List<Integer> listIdTypes = new ArrayList<Integer>();

      listIdTypes.add(1);
      listIdTypes.add(2);
      listIdTypes.add(4);

      when(objectDAOMock.getObjectsSortedByTypes(listIdTypes)).thenReturn(objects);

      // ACT
      List<ObjectDto> result = objectUCC.getObjectsSortedByTypes(listIdTypes);

      // ASSERT
      assertEquals(objects.size(), result.size());
      assertEquals(objects, result);
      Mockito.verify(objectDAOMock).getObjectsSortedByTypes(listIdTypes);
    }

    @DisplayName("the returned objects list is empty, should throw an error")
    @Test
    void testGetObjectsSortedByTypesNotFound() {
      // ARRANGE
      ArrayList<Integer> listId = new ArrayList<Integer>();
      listId.add(1);
      listId.add(3);
      listId.add(4);
      List<ObjectDto> objects = new ArrayList<>();
      when(objectDAOMock.getObjectsSortedByTypes(listId)).thenReturn(objects);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsSortedByTypes(listId);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetObjectsSortedByTypesWithFatalException() {
      ArrayList<Integer> listId = new ArrayList<Integer>();
      listId.add(1);
      listId.add(3);
      listId.add(4);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.getObjectsSortedByTypes(listId)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsSortedByTypes(listId);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class UpdateObjectPictureTests {

    @DisplayName("testing the update picture succes")
    @Test
    void testUpdateObjectPictureSuccess() {
      // ARRANGE
      ObjectDto validObject = objectFactory.getObject();
      int validId = 1;
      when(objectDAOMock.getOneObjectById(validId)).thenReturn(validObject);

      when(objectDAOMock.updateOneObject(validObject)).thenReturn(validObject);

      // ACT
      ObjectDto result = objectUCC.updateObjectPicture(validId, "test");

      // ASSERT
      assertEquals(result.getPicturePath(), "test");
      Mockito.verify(objectDAOMock).updateOneObject(validObject);
    }

    @DisplayName("the returned objects list is empty, should throw an error")
    @Test
    void testUpdateObjectPictureNotFound() {
      // ARRANGE
      int notFoundId = 1;
      when(objectDAOMock.getOneObjectById(notFoundId)).thenReturn(null);

      // ACT
      Executable runnable = () -> objectUCC.updateObjectPicture(notFoundId, "test");

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testUpdateObjectPictureWithFatalException() {
      ObjectDto validObject = objectFactory.getObject();
      int validId = 1;
      when(objectDAOMock.getOneObjectById(validId)).thenReturn(validObject);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.updateOneObject(validObject)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.updateObjectPicture(validId, "test");

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetNumberSoldObjectsTests {

    @DisplayName("testing that all the objects are returned ")
    @Test
    void testGetNumberSoldObjectsSuccess() {
      // ARRANGE
      int numberObjectSold = 26;

      when(objectDAOMock.getNumberOfSoldObjects()).thenReturn(numberObjectSold);

      // ACT
      int result = objectUCC.getNumberOfSoldObjects();

      // ASSERT
      assertEquals(numberObjectSold, result);
      Mockito.verify(objectDAOMock).getNumberOfSoldObjects();
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetNumberSoldObjectsWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.getNumberOfSoldObjects()).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getNumberOfSoldObjects();

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetAllObjectFromOneUserTests {

    @DisplayName("this test try to get all the objects of one user"
        + "it should return the list of objects of the user ")
    @Test
    void testGetAllObjectsFromIdSuccess() {
      // ARRANGE
      int userId1 = 1;
      int userId2 = 2;

      ObjectDto object1 = objectFactory.getObject();
      ObjectDto object2 = objectFactory.getObject();
      ObjectDto object3 = objectFactory.getObject();
      object1.setOwnerId(userId1);
      object2.setOwnerId(userId1);
      object3.setOwnerId(userId2);
      List<ObjectDto> objects = new ArrayList<>();
      objects.add(object1);
      objects.add(object2);
      objects.add(object3);

      when(objectDAOMock.getAllObjectsFromOneUser(userId1))
          .thenReturn(objects.stream()
              .filter(obj -> obj.getOwnerId() == userId1)
              .collect(Collectors.toList()));

      // ACT
      List<ObjectDto> result = objectUCC.getAllObjectsFromOneUser(userId1);

      // ASSERT
      assertEquals(2, result.size());
    }

    @DisplayName("this test try to get all the objects of one user"
        + "if the no objects was found, it should throw a NotFoundException")
    @Test
    void testGetAllObjectsFromIdEmpty() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();

      when(objectDAOMock.getAllObjectsFromOneUser(id)).thenReturn(objects);

      // ACT
      Executable runnable = () -> objectUCC.getAllObjectsFromOneUser(id);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testDenyWithFatalException() {
      int validId = 3;
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(objectDAOMock.getAllObjectsFromOneUser(validId)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getAllObjectsFromOneUser(validId);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class CreateObjectsTests {

    @DisplayName("test update succes")
    @Test
    public void testCreateSucces() {
      ObjectDto createdObject = objectFactory.getObject();
      createdObject.setId(2);
      when(objectDAOMock.createObject(createdObject)).thenReturn(createdObject);

      ObjectDto returnedObject = objectUCC.createObject(createdObject);

      // ASSERT
      assertAll(
          () -> assertEquals(createdObject, returnedObject),
          () -> Mockito.verify(objectDAOMock).createObject(createdObject)
      );

    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testCreateWithException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      ObjectDto validObject = objectFactory.getObject();
      when(objectDAOMock.createObject(validObject)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.createObject(validObject);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }

    @DisplayName("test when no object associated")
    @Test
    public void testCreateWithNotFound() {
      ObjectDto notFoundObject = objectFactory.getObject();
      when(objectDAOMock.createObject(notFoundObject)).thenReturn(null);

      // ACT
      Executable runnable = () -> objectUCC.createObject(notFoundObject);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }
  }

  @Nested
  class GetObjectsForRecoveryDateTests {

    @DisplayName("Get objects for recovery date test")
    @Test
    void testGetObjectsForARecoveryDateSucces() {

      LocalDateTime searchedDate = LocalDateTime.of(2023, 3, 24, 0, 0, 0, 0);
      LocalDateTime notSearchedDate = LocalDateTime.of(2023, 3, 28, 0, 0, 0, 0);

      ObjectDto object1 = objectFactory.getObject();
      ObjectDto object2 = objectFactory.getObject();
      ObjectDto object3 = objectFactory.getObject();
      object1.setRecoveryDate(searchedDate);
      object2.setRecoveryDate(notSearchedDate);
      object3.setRecoveryDate(searchedDate);
      List<ObjectDto> objects = new ArrayList<>();
      objects.add(object1);
      objects.add(object2);
      objects.add(object3);

      when(objectDAOMock.getObjectsForRecoveryDate(searchedDate))
          .thenReturn(objects.stream()
              .filter(obj -> obj.getRecoveryDate() == searchedDate)
              .collect(Collectors.toList()));

      // ACT
      List<ObjectDto> result = objectUCC.getObjectsForRecoveryDate(searchedDate);

      // ASSERT
      assertEquals(2, result.size());
    }

    @DisplayName("testing with no objects for this date")
    @Test
    void testGetObectsForRecoveryWithNoCorrespondance() {
      // ARRANGE
      List<ObjectDto> objects = new ArrayList<>();
      LocalDateTime badDate = LocalDateTime.of(2023, 3, 24, 0, 0, 0, 0);
      when(objectDAOMock.getObjectsForRecoveryDate(badDate)).thenReturn(objects);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsForRecoveryDate(badDate);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetObjectForRecoveryDateWithFatalException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      LocalDateTime date = LocalDateTime.of(2022, 3, 10, 10, 30);
      when(objectDAOMock.getObjectsForRecoveryDate(date)).thenThrow(exception);

      // ACT
      Executable runnable = () -> objectUCC.getObjectsForRecoveryDate(date);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }
}
