package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import be.vinci.pae.domain.interfaces.User.UserRole;
import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.domain.interfaces.UserFactory;
import be.vinci.pae.domain.interfaces.UserUCC;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.MyFatalException;
import be.vinci.pae.utils.TestApplicationBinder;
import jakarta.ws.rs.NotAuthorizedException;
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
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

/**
 * this class is used to test the implementation of UserUCC.
 */
public class UserUCCImplTest {

  private static UserDAO userDAOMock;
  private static UserUCC userUCC;
  private static UserFactory userFactory;
  private UserDto expectedUser;

  @BeforeAll
  static void beforeAll() {
    TestApplicationBinder appBinder = new TestApplicationBinder();

    ServiceLocator locator = ServiceLocatorUtilities.bind(appBinder);
    userFactory = locator.getService(UserFactory.class);
    userUCC = locator.getService(UserUCC.class);
    userDAOMock = locator.getService(UserDAO.class);
  }

  @BeforeEach
  void beforeEach() {
    expectedUser = userFactory.getUser();
    Mockito.reset(userDAOMock);
  }

  @Nested
  class Register {

    @Test
    void testRegisterWithCorrectCredentials() {
      // ARRANGE
      expectedUser.setId(1);
      expectedUser.setFirstname("firstname");
      expectedUser.setLastname("lastname");
      expectedUser.setEmail("email1@gmail.com");
      expectedUser.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

      Mockito.when(userDAOMock.createOneUser(expectedUser)).thenReturn(expectedUser);

      // ACT
      UserDto u = userUCC.register(expectedUser);

      // ASSERT
      assertEquals(u, expectedUser);
    }

    @Test
    void testRegisterWithMailAlreadyExists() {
      // ARRANGE
      expectedUser.setId(1);
      expectedUser.setFirstname("firstname");
      expectedUser.setLastname("lastname");
      expectedUser.setEmail("email1@gmail.com");
      expectedUser.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

      Mockito.when(userDAOMock.createOneUser(expectedUser))
          .thenThrow(new IllegalArgumentException());

      // ACT
      Executable runnable = () -> userUCC.register(expectedUser);

      // ASSERT
      assertThrows(IllegalArgumentException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testRegisterWithException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      UserDto validUser = userFactory.getUser();
      when(userDAOMock.createOneUser(validUser)).thenThrow(exception);

      // ACT
      Executable runnable = () -> userUCC.register(validUser);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class Login {

    @DisplayName("this test try to log a user with correct credentials")
    @Test
    void testLoginWithCorrectCredentialsShouldReturnCorrespondingUser() {
      // ARRANGE
      String email = "jo@ok.com";
      String password = "password";

      expectedUser.setId(1);
      expectedUser.setEmail(email);
      expectedUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

      Mockito.when(userDAOMock.getOneUserByEmail(email)).thenReturn(expectedUser);

      // ACT
      UserDto actualUser = userUCC.login(email, password);

      // ASSERT
      assertEquals(actualUser, expectedUser);
    }

    @DisplayName("this test try to log a user, if the email address doesn't exist"
        + "should trow a NotAuthorizedException")
    @Test
    void testLoginWithInvalidEmailShouldThrowNotAuthorizedException() {
      // ARRANGE
      String email = "nonExistent@noMail.com";
      String password = "password";

      Mockito.when(userDAOMock.getOneUserByEmail(email)).thenReturn(null);

      // ACT
      Executable runnable = () -> userUCC.login(email, password);

      // ASSERT
      assertThrows(NotAuthorizedException.class, runnable);
    }

    @DisplayName("this test try to log a user with incorrect credentials"
        + "should throw a NotAuthorizedException ")
    @Test
    void testLoginWithIncorrectCredentialsShouldThrowNotAuthorizedException() {
      // ARRANGE
      String email = "jo@ok.com";

      expectedUser.setId(1);
      expectedUser.setEmail(email);
      expectedUser.setPassword(
          BCrypt.hashpw("notCorrespondingPassword", BCrypt.gensalt())
      );

      Mockito.when(userDAOMock.getOneUserByEmail(email)).thenReturn(expectedUser);

      // ACT
      Executable runnable = () -> userUCC.login(email, "passwordInvalid");

      // ASSERT
      assertThrows(NotAuthorizedException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testLoginWithException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      String validEmail = "r@gmail.com";
      when(userDAOMock.getOneUserByEmail(validEmail)).thenThrow(exception);

      // ACT
      Executable runnable = () -> userUCC.login(validEmail, "123");

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class Me {

    @DisplayName("test that try to find the connected user "
        + "in the db and return the user if it was found ")
    @Test
    void testMeWithCorrectIdShouldReturnCorrespondingUser() {
      // ARRANGE
      int correctId = 1;

      expectedUser.setId(correctId);
      expectedUser.setFirstname("Tom");
      when(userDAOMock.getOneUserById(correctId)).thenReturn(expectedUser);

      // ACT
      UserDto actualUser = userUCC.getOneUserById(correctId);

      // ASSERT
      assertEquals(actualUser, expectedUser);
    }

    @DisplayName("test that try to find the connected user in the db "
        + "throw a NotFoundException if no user was found ")
    @Test
    void testMeWithIncorrectIdShouldThrowNotFoundException() {
      // ARRANGE
      int wrongId = -10;
      when(userDAOMock.getOneUserById(wrongId)).thenReturn(null);

      // ACT
      Executable runnable = () -> userUCC.getOneUserById(wrongId);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }
  }

  @Nested
  class UpdateUserTests {

    @DisplayName("test update succes")
    @Test
    public void testUpdateSucces() {
      UserDto updateUser = userFactory.getUser();
      updateUser.setId(2);
      updateUser.setFirstname("Maxime");
      updateUser.setLastname("Vlaminck");
      updateUser.setEmail("m@gmail.com");
      updateUser.setPassword("test");
      updateUser.setRegistrationDate(LocalDateTime.now());
      updateUser.setUserRole(UserRole.USER);
      when(userDAOMock.updateUser(updateUser)).thenReturn(updateUser);

      UserDto returnedUser = userUCC.updateUser(updateUser);

      // ASSERT
      assertAll(
          () -> assertEquals(updateUser, returnedUser),
          () -> Mockito.verify(userDAOMock).updateUser(updateUser)
      );

    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testupdateWithException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      UserDto validUser = userFactory.getUser();
      when(userDAOMock.updateUser(validUser)).thenThrow(exception);

      // ACT
      Executable runnable = () -> userUCC.updateUser(validUser);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }

    @DisplayName("test when no user associated")
    @Test
    public void testupdateWithNotFound() {
      UserDto notFoundUser = userFactory.getUser();
      when(userDAOMock.updateUser(notFoundUser)).thenReturn(null);

      // ACT
      Executable runnable = () -> userUCC.updateUser(notFoundUser);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }
  }

  @Nested
  class UpdateUserPictureTests {

    @DisplayName("test updatePicture succes")
    @Test
    public void testUpdatePictureSucces() {
      UserDto updateUser = userFactory.getUser();
      updateUser.setId(2);
      when(userDAOMock.getOneUserById(2)).thenReturn(updateUser);

      userUCC.updateUserPicture(2, "./CheminBidonEau");

      // ASSERT
      assertAll(
          () -> assertTrue(updateUser.getPicture().equals("./CheminBidonEau")),
          () -> Mockito.verify(userDAOMock).updateUser(updateUser)
      );

    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testupdatePictureWithException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      UserDto user = userFactory.getUser();
      user.setId(2);
      when(userDAOMock.getOneUserById(user.getId())).thenReturn(user);
      when(userDAOMock.updateUser(user)).thenThrow(exception);

      // ACT
      Executable runnable = () -> userUCC.updateUserPicture(user.getId(), "chemin");

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class ConfirmHelperRegistration {

    @DisplayName("this test try to confirm the registration of a USER to a HELPER")
    @Test
    public void testConfirmHelperRegistrationOfAnUser() {
      // ARRANGE
      UserDto user = userFactory.getUser();
      user.setId(1);
      user.setUserRole(UserRole.USER);
      when(userDAOMock.getOneUserById(1)).thenReturn(user);
      when(userDAOMock.updateUser(user)).thenReturn(user);

      // ACT
      UserDto result = userUCC.confirmHelperRegistration(1);

      // ASSERT
      assertEquals(UserRole.HELPER, result.getUserRole());
    }

    @DisplayName("this test try to confirm the registration of a HELPER "
        + "it should throw a NotAuthorizedException")
    @Test
    public void testConfirmHelperRegistrationOfAHelper() {
      // ARRANGE
      UserDto user = userFactory.getUser();
      user.setId(1);
      user.setUserRole(UserRole.HELPER);
      when(userDAOMock.getOneUserById(1)).thenReturn(user);
      when(userDAOMock.updateUser(user)).thenReturn(user);

      // ACT
      Executable runnable = () -> userUCC.confirmHelperRegistration(1);

      // ASSERT
      assertThrows(NotAuthorizedException.class, runnable);
    }

    @DisplayName("this test try to confirm the registration of a RESPONSIBLE "
        + "it should throw a NotAuthorizedException")
    @Test
    public void testConfirmHelperRegistrationOfAnResponsible() {
      // ARRANGE
      UserDto user = userFactory.getUser();
      user.setId(1);
      user.setUserRole(UserRole.RESPONSIBLE);
      when(userDAOMock.getOneUserById(1)).thenReturn(user);
      when(userDAOMock.updateUser(user)).thenReturn(user);

      // ACT
      Executable runnable = () -> userUCC.confirmHelperRegistration(1);

      // ASSERT
      assertThrows(NotAuthorizedException.class, runnable);
    }

    @DisplayName("this test try to confirm the registration of a HELPER "
        + "it should throw a NotFoundException if an error occurred with the update")
    @Test
    public void testConfirmHelperRegistration() {
      // ARRANGE
      UserDto user = userFactory.getUser();
      user.setId(1);
      user.setUserRole(UserRole.USER);
      when(userDAOMock.getOneUserById(1)).thenReturn(user);
      when(userDAOMock.updateUser(user)).thenReturn(null);

      // ACT
      Executable runnable = () -> userUCC.confirmHelperRegistration(1);

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test confirm helper with an non existing user")
    @Test
    public void testConfirmHelperRegistrationOfNotFound() {
      int notFoundId = 1560;
      UserDto notFoundUser = userFactory.getUser();
      notFoundUser.setId(notFoundId);
      when(userDAOMock.getOneUserById(notFoundId)).thenReturn(null);

      // ACT
      Executable runnable = () -> userUCC.confirmHelperRegistration(notFoundId);

      // ASSERT
      assertAll(
          () -> assertThrows(NotFoundException.class, runnable),
          () -> Mockito.verify(userDAOMock, times(0)).updateUser(notFoundUser)
      );
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testConfirmHelperRegistrationWithException() {
      int validUserId = 123;
      UserDto user = userFactory.getUser();
      user.setId(validUserId);
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(userDAOMock.getOneUserById(validUserId)).thenReturn(user);
      when(userDAOMock.updateUser(user)).thenThrow(exception);

      // ACT
      Executable runnable = () -> userUCC.confirmHelperRegistration(validUserId);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }

  }

  @Nested
  class GetAllUsers {

    @DisplayName("this test should return a list of all the users ")
    @Test
    public void testGetAllUsers() {
      // ARRANGE
      List<UserDto> users = new ArrayList<>();
      users.add(mock(UserDto.class));
      users.add(mock(UserDto.class));
      when(userDAOMock.getAllUsers()).thenReturn(users);

      // ACT
      List<UserDto> result = userUCC.getAllUsers();

      // ASSERT
      assertEquals(users.size(), result.size());
      assertEquals(users.get(0), result.get(0));
      assertEquals(users.get(1), result.get(1));
    }

    @DisplayName("this test should return a list of all the users "
        + "If the list is empty, it should throw a NotFoundException")
    @Test
    public void testGetAllUsersEmptyList() {
      // ARRANGE
      List<UserDto> users = new ArrayList<>();
      when(userDAOMock.getAllUsers()).thenReturn(users);

      // ACT
      Executable runnable = () -> userUCC.getAllUsers();

      // ASSERT
      assertThrows(NotFoundException.class, runnable);
    }

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetAllUsersWithException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      when(userDAOMock.getAllUsers()).thenThrow(exception);

      // ACT
      Executable runnable = () -> userUCC.getAllUsers();

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }

  @Nested
  class GetOneUserByIdTest {

    @DisplayName("test when MyFatalException is throw")
    @Test
    public void testGetOneUserByIdTestWithException() {
      MyFatalException exception = new MyFatalException("Simulating a MyFatalException");
      int validId = 2;
      when(userDAOMock.getOneUserById(validId)).thenThrow(exception);

      // ACT
      Executable runnable = () -> userUCC.getOneUserById(validId);

      // ASSERT
      assertThrows(MyFatalException.class, runnable);
    }
  }
}