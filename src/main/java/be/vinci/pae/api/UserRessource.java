package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.Responsible;
import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.domain.interfaces.UserFactory;
import be.vinci.pae.domain.interfaces.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyLogger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Annotation to use "/users" path.
 */
@Singleton
@Path("/users")
public class UserRessource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final int jwtDurationSecond = Integer.parseInt(Config.getProperty("JWTDurationSecond"));
  @Inject
  ObjectMapper mapper;
  @Inject
  private UserFactory userFactory;
  @Inject
  private UserUCC userUCC;

  /**
   * POST route used to connect a user and send its information.
   *
   * @param json given by grizzly
   * @return user to return to client
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    MyLogger.info(" The user is in users/login route");
    // CHECK VALID REQUIRED INPUTS
    if (!json.hasNonNull("email") || !json.hasNonNull("password")
        || json.get("email").asText().isBlank() || json.get("password").asText().isBlank()
        || !json.get("email").asText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|be)$")) {
      MyLogger.warning("email or password required");
      throw new BadRequestException("email or password required");
    }

    // GET INPUT
    String email = json.get("email").asText();
    String password = json.get("password").asText();

    UserDto user = userUCC.login(email, password);
    return userDtoToObjectNode(user);
  }

  /**
   * POST route used to update information of the user connected.
   *
   * @param request given by grizzly
   * @param json    given by grizzly
   * @return user to return to client
   */
  @POST
  @Path("updateUser")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode updateUser(JsonNode json, @Context ContainerRequest request) {
    MyLogger.info(" The user is in users/updateUser route");

    UserDto userInput = (UserDto) request.getProperty("user");
    // checker le mot de passe entré est le bon :
    if (!BCrypt.checkpw(json.get("password").asText(), userInput.getPassword())) {
      throw new IllegalArgumentException(
          "Le mot de passe entré est pas bon"); //TODO checker l'erreur de la bonne manière
    }
    if (json.hasNonNull("firstname") && !json.get("firstname").asText().isBlank()) {
      userInput.setFirstname(json.get("firstname").asText());
    }
    if (json.hasNonNull("lastname") && !json.get("lastname").asText().isBlank()) {
      userInput.setLastname(json.get("lastname").asText());
    }
    // le nouveau mot de passe
    if (json.hasNonNull("newPassword") && !json.get("newPassword").asText().isBlank()) {
      System.out.println(json.get("newPassword").asText());
      userInput.setPassword(BCrypt.hashpw(json.get("newPassword").asText(), BCrypt.gensalt()));
    }
    if (json.hasNonNull("email") && !json.get("email").asText().isBlank()) {
      userInput.setEmail(json.get("email").asText());
    }
    if (json.hasNonNull("gsm") && !json.get("gsm").asText().isBlank()) {
      userInput.setGsm(json.get("gsm").asText());
    }
    if (json.hasNonNull("gsm") && !json.get("gsm").asText().isBlank()) {
      userInput.setGsm(json.get("gsm").asText());
    }
    userUCC.updateUser(userInput);
    return userDtoToObjectNode(userInput);
  }


  /**
   * POST route used to keep a user connected and send its information again.
   *
   * @param request given by grizzly
   * @return user to return to client
   */
  @POST
  @Path("me")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDto me(@Context ContainerRequest request) {
    MyLogger.info(" The user is in users/me route");

    return (UserDto) request.getProperty("user");
  }

  /**
   * Uploads and update a file to the server and saves it to a specified location.
   *
   * @param request         given by grizzly
   * @param file            the input stream containing the file to upload
   * @param fileDisposition the disposition of the uploaded file, including its original filename
   * @return a response indicating the status of the upload operation
   * @throws RuntimeException if there is an error copying the file to the server
   */
  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDto uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @Context ContainerRequest request) {
    MyLogger.info(" The user is in users/upload route");

    UserDto user = (UserDto) request.getProperty("user");
    int userId = user.getId();

    String fileNameUser = fileDisposition.getFileName();
    String extension = FilenameUtils.getExtension(fileNameUser);
    String newFileName = UUID.randomUUID() + "." + extension;
    String absolutePath = Paths.get("").toAbsolutePath().toString() + "/imgUsers/" + newFileName;

    try {
      Files.copy(file, Paths.get(absolutePath));
    } catch (IOException e) {
      MyLogger.warning(" error in users/upload route");
      throw new RuntimeException(e);
    }

    return userUCC.updateUserPicture(userId, newFileName);
  }


  /**
   * Returns a Response object containing the image file specified by the given userId.
   *
   * @param userId the id of the user to load the image for
   * @return a Response object containing the image file
   */
  @GET
  @Path("load/{userId}")
  @Produces("image/png")
  public Response loadImage(@PathParam("userId") String userId) {
    MyLogger.info(" The user is in users/load/{userId} route");

    UserDto user = userUCC.getOneUserById(Integer.parseInt(userId));
    String imagePath = "/imgUsers/" + user.getPicture();
    String absolutePath = Paths.get("").toAbsolutePath().toString() + imagePath;

    File file = new File(absolutePath);
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      MyLogger.warning("error in users/load/{userId} route");
      e.printStackTrace();
    }

    return Response.ok(inputStream).build();
  }


  /**
   * Path used to register a new user.
   *
   * @param json given by grizzly
   * @return inserted user to return to the client
   */
  @POST
  @Path("register")
  @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode register(JsonNode json) {
    MyLogger.info(" The user is in users/register route");

    // CHECK VALID REQUIRED INPUTS
    if (!json.hasNonNull("firstname")
        || !json.hasNonNull("lastname")
        || !json.hasNonNull("email")
        || !json.hasNonNull("password")
        || !json.hasNonNull("gsm")) {
      MyLogger.warning("some field are null");
      throw new BadRequestException("some field are null");
    }

    if (json.get("firstname").asText().isBlank()
        || json.get("lastname").asText().isBlank()
        || json.get("email").asText().isBlank()
        || !json.get("email").asText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|be)$")
        || json.get("password").asText().isBlank()
        || json.get("gsm").asText().isBlank()) {
      MyLogger.warning("some field are incorrect");
      throw new BadRequestException("some field are incorrect");
    }

    // UCC
    UserDto userInput = userFactory.getUser();
    userInput.setFirstname(json.get("firstname").asText());
    userInput.setLastname(json.get("lastname").asText());
    userInput.setEmail(json.get("email").asText());
    userInput.setGsm(json.get("gsm").asText());
    //userInput.setRegistrationDate(json.get("registrationDate").asText());
    //userInput.setUserRole(json.get("userRole").asText());
    //userInput.setPicture(newFileName);
    userInput.setPassword(json.get("password").asText());
    userInput.setGsm(json.get("gsm").asText());

    UserDto userReturn = userUCC.register(userInput);

    // transform and send to the client
    return userDtoToObjectNode(userReturn);
  }

  /**
   * POST route used to keep a user connected and send its information again.
   *
   * @param json given by grizzly
   * @return user to return to client
   */
  @POST
  @Path("makeHelper")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Responsible
  public UserDto confirmHelperRegistration(JsonNode json) {
    MyLogger.info(" The user is in users/makeHelper route");

    // CHECK VALID ID
    if (!json.hasNonNull("idHelper") || json.get("idHelper").asText().isBlank()) {
      MyLogger.warning("Invalid user  ID provided : failed to confirm this user");
      throw new BadRequestException("Invalid user  ID provided : failed to confirm this user");
    }

    // GET INPUT
    int idHelper = Integer.parseInt(json.get("idHelper").asText());

    return userUCC.confirmHelperRegistration(idHelper);
  }


  /**
   * POST route used to keep a user connected and send its information again.
   *
   * @param json given by grizzly
   * @return user to return to client
   */
  @POST
  @Path("makeResponsible")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Responsible
  public UserDto confirmResponsibleRegistration(JsonNode json) {
    MyLogger.info(" The user is in users/makeResponsible route");

    // CHECK VALID ID
    if (!json.hasNonNull("idHelper") || json.get("idHelper").asText().isBlank()) {
      MyLogger.warning("Invalid user  ID provided : failed to confirm this user");
      throw new BadRequestException("Invalid user  ID provided : failed to confirm this user");
    }

    // GET INPUT
    int idHelper = Integer.parseInt(json.get("idHelper").asText());

    return userUCC.confirmResponsibleRegistration(idHelper);
  }


  /**
   * GET route used to get all users.
   *
   * @return list with all users
   */
  @GET
  @Path("listUsers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Responsible
  public List<UserDto> listUsers() {
    MyLogger.info(" The user is in users/listUsers route");
    return userUCC.getAllUsers();
  }


  /**
   * GET route used to get a user by id.
   *
   * @param userId given by grizzly
   * @return user to return to client
   */
  @GET
  @Path("getUser/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public UserDto getUserById(@PathParam("userId") int userId) {
    MyLogger.info(" The user is in users/getUser/" + userId + " route");
    return userUCC.getOneUserById(userId);
  }


  /**
   * Private method to convert a UserDto object to a JSON ObjectNode that will be transferred to the
   * frontend app.
   *
   * @param user UserDto object to be converted
   * @return ObjectNode containing the user's data and a token
   */
  private ObjectNode userDtoToObjectNode(UserDto user) {
    // create a node object that will be transferred and used in the frontend app
    ObjectNode result = mapper.createObjectNode();
    result.put("token", makeToken(user));
    result.putPOJO("user", user);
    return result;
  }

  /**
   * Private method to generate a JWT token for a given UserDto object.
   *
   * @param user UserDto object for which to generate the token
   * @return Generated JWT token
   */
  private String makeToken(UserDto user) {
    String token;
    try {
      token = JWT.create().withIssuer("auth0").withClaim("id", user.getId())
          .withIssuedAt(Date.from(Instant.now()))
          .withExpiresAt(Date.from(Instant.now().plusSeconds(jwtDurationSecond)))
          .sign(this.jwtAlgorithm);
      return token;
    } catch (Exception e) {
      MyLogger.warning("Unable to create token");
      System.out.println("Unable to create token");
      return null; // TODO throw correct error
    }
  }
}