package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.Responsible;
import be.vinci.pae.api.filters.ResponsibleOrHelper;
import be.vinci.pae.domain.interfaces.AvailabilityDto;
import be.vinci.pae.domain.interfaces.AvailabilityUCC;
import be.vinci.pae.domain.interfaces.NotificationUCC;
import be.vinci.pae.domain.interfaces.Object.STATE;
import be.vinci.pae.domain.interfaces.ObjectDto;
import be.vinci.pae.domain.interfaces.ObjectFactory;
import be.vinci.pae.domain.interfaces.ObjectUCC;
import be.vinci.pae.domain.interfaces.TypeObjectDto;
import be.vinci.pae.domain.interfaces.UserDto;
import be.vinci.pae.utils.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * Annotation to use "/objets" path.
 */
@Singleton
@Path("/objects")
public class ObjectRessource {

  @Inject
  private ObjectUCC objectUCC;
  @Inject
  private AvailabilityUCC availabilityUCC;
  @Inject
  private ObjectFactory objectFactory;

  @Inject
  private NotificationUCC notificationUCC;


  /**
   * POST route used to update an object's state to "accept" state.
   *
   * @param json the JSON object provided by Grizzly
   * @return JSON object with the new state
   * @throws BadRequestException if the ID is missing or blank
   */
  @POST
  @Path("accept")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Responsible
  public ObjectDto acceptObject(JsonNode json) {
    MyLogger.info(" The user is in objects/accept route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in accept request");
      throw new BadRequestException("Invalid ID provided in accept request");
    }

    // GET INPUT
    int objectId = Integer.parseInt(json.get("id").asText());

    if (objectUCC.getOneObjectById(objectId).getOwnerId() != 0) {
      notificationUCC.createNotificationForObjectAccepted(objectUCC.getOneObjectById(objectId));
    }
    return objectUCC.acceptObjectProposition(objectId);
  }

  /**
   * POST route used to update the object state to deny state.
   *
   * @param json JSON object received from Grizzly
   * @return JSON object with the new state
   * @throws BadRequestException if the ID is missing or blank
   */
  @POST
  @Path("deny")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Responsible
  public ObjectDto denyObject(JsonNode json) {
    MyLogger.info(" The user is in objects/deny route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in deny request");
      throw new BadRequestException("Invalid ID provided in deny request");
    }
    // CHECK VALID REFUSAL REASON
    if (!json.hasNonNull("refusalReason") || json.get("refusalReason").asText().isBlank()) {
      MyLogger.warning("Invalid refusal reason provided in deny request");
      throw new BadRequestException("Invalid refusal reason provided in deny request");
    }

    // GET INPUT
    int objectId = Integer.parseInt(json.get("id").asText());
    String refusalReason = json.get("refusalReason").asText();

    notificationUCC.createNotificationForObjectRefused(objectUCC.getOneObjectById(objectId));

    return objectUCC.denyObjectState(objectId, refusalReason);
  }


  /**
   * POST route used to update the object state to workshop state.
   *
   * @param json JSON object received from Grizzly
   * @return JSON object with the new state
   */
  @POST
  @Path("workshop")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public ObjectDto workshopObject(JsonNode json) {
    MyLogger.info(" The user is in objects/workshop route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in workshop request");
      throw new BadRequestException("Invalid ID provided in workshop request");
    }

    // GET INPUT
    int objectId = Integer.parseInt(json.get("id").asText());

    return objectUCC.workshopObjectState(objectId);
  }


  /**
   * POST route used to update the object state to store state.
   *
   * @param json JSON object received from Grizzly
   * @return JSON object with the new state
   */
  @POST
  @Path("store")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public ObjectDto storeObject(JsonNode json) {
    MyLogger.info(" The user is in objects/store route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in store request");
      throw new BadRequestException("Invalid ID provided in store request");
    }

    // GET INPUT
    int objectId = Integer.parseInt(json.get("id").asText());

    return objectUCC.storeObjectState(objectId);
  }

  /**
   * Method used in soldInStore and saleObject so there is no duplicate code.
   *
   * @param json JSON object received from Grizzly.
   * @param msg  msg that is going in the logger file.
   */
  public void saleAndSoldObject(JsonNode json, String msg) {
    MyLogger.info(" The user is in objects/ " + msg + " route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in " + msg + " request");
      throw new BadRequestException("Invalid ID provided in " + msg + " request");
    }
    // CHECK VALID SELLING PRICE
    if (!json.hasNonNull("sellingPrice") || json.get("sellingPrice").asText().isBlank()) {
      MyLogger.warning("Invalid sellingPrice provided in " + msg + " request");
      throw new BadRequestException("Invalid sellingPrice provided in " + msg + " request");
    }
  }

  /**
   * POST route used to update the object state to sale state.
   *
   * @param json JSON object received from Grizzly.
   * @return JSON object with the new state
   */
  @POST
  @Path("sale")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public ObjectDto saleObject(JsonNode json) {
    saleAndSoldObject(json, "sale");
    // GET INPUT
    double sellingPrice = json.get("sellingPrice").asDouble();
    int objectId = Integer.parseInt(json.get("id").asText());
    return objectUCC.saleObjectState(objectId, sellingPrice);
  }


  /**
   * POST route used to update the object state to sale state.
   *
   * @param json JSON object received from Grizzly.
   * @return JSON object with the new state
   */
  @POST
  @Path("soldInStore")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Responsible
  public ObjectDto soldInStore(JsonNode json) {
    saleAndSoldObject(json, "soldInStore");
    // GET INPUT
    double sellingPrice = json.get("sellingPrice").asDouble();
    int objectId = Integer.parseInt(json.get("id").asText());

    return objectUCC.soldInStore(objectId, sellingPrice);
  }


  /**
   * POST route used to update the object state to sold state.
   *
   * @param json JSON object received from Grizzly
   * @return JSON object with the new state
   */
  @POST
  @Path("sold")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public ObjectDto soldObject(JsonNode json) {
    MyLogger.info(" The user is in objects/sold route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in sold request");
      throw new BadRequestException("Invalid ID provided in sold request");
    }

    // GET INPUT
    int objectId = Integer.parseInt(json.get("id").asText());

    return objectUCC.soldObjectState(objectId);
  }


  /**
   * GET route used to get all the existing objects.
   *
   * @return a list of all the objects
   */
  @GET
  @Path("getObjectList")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDto> getObjects() {
    MyLogger.info(" The user is in objects/getObjectList route");

    return objectUCC.getAllObjects();
  }

  /**
   * GET route used to get all the data of an object by its id.
   *
   * @param id the ID of the object to get
   * @return JSON object with all its data
   */
  @GET
  @Path("getOneObject")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDto getOneObject(@QueryParam("id") String id) {
    MyLogger.info(" The user is in objects/getOneObject route");

    // CHECK VALID ID
    if (id == null || id.isBlank()) {
      MyLogger.warning("Invalid ID provided in getOneObject request");
      throw new BadRequestException("Invalid ID provided in getOneObject request");
    }

    // GET INPUT
    int objectId = Integer.parseInt(id);

    return objectUCC.getOneObjectById(objectId);
  }

  /**
   * upload and update a file to the server and saves it to a specified location.
   *
   * @param objectId        the id of the object
   * @param file            the input stream containing the file to upload
   * @param fileDisposition the disposition of the uploaded file, including its original filename
   * @return a response indicating the status of the upload operation
   * @throws RuntimeException if there is an error copying the file to the server
   */
  @POST
  @Path("/upload/{objectId}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDto uploadFile(@PathParam("objectId") int objectId,
      @FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) {

    MyLogger.info(" The user is in objects/update/{objectId} route");

    String fileName = fileDisposition.getFileName();
    String extension = FilenameUtils.getExtension(fileName);
    String newFileName = UUID.randomUUID() + "." + extension;
    String absolutePath = Paths.get("").toAbsolutePath().toString() + "/imgObjects/" + newFileName;

    try {
      Files.copy(file, Paths.get(absolutePath));
    } catch (IOException e) {
      MyLogger.warning("error in objects/update/{objectId} route");
      throw new RuntimeException(e);
    }

    return objectUCC.updateObjectPicture(objectId, newFileName);
  }

  /**
   * Returns a Response object containing the image file specified by the given objectId.
   *
   * @param objectId the id of the object to load the image for
   * @return a Response object containing the image file
   */
  @GET
  @Path("load/{objectId}")
  @Produces("image/png")
  public Response loadImage(@PathParam("objectId") String objectId) {
    MyLogger.info(" The user is in objects/load/{objectId} route");

    ObjectDto object = objectUCC.getOneObjectById(Integer.parseInt(objectId));
    String objectPath = "/imgObjects/" + object.getPicturePath();
    String absolutePath = Paths.get("").toAbsolutePath() + objectPath;

    File file = new File(absolutePath);
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      MyLogger.warning("error in objects/load/{objectId} route");

      e.printStackTrace();
    }

    return Response.ok(inputStream).build();
  }


  /**
   * POST route used to create a new object.
   *
   * @param json JSON object received from frontend
   * @return JSON object with the new object's details
   */
  @POST
  @Path("createObject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectDto createObject(JsonNode json) {

    MyLogger.info(" The user is in objects/createObject route");

    if (!json.hasNonNull("name") || json.get("name").asText().isBlank()
        || !json.hasNonNull("description") || json.get("description").asText().isBlank()
        || !json.hasNonNull("type_object_id") || json.get("type_object_id").asInt() <= 0
        || !json.hasNonNull("availability_id") || json.get("availability_id").asInt() <= 0) {
      MyLogger.warning("Invalid object details provided in create request");
      throw new BadRequestException("Invalid object details provided in create request");
    }

    // UCC
    ObjectDto objectInput = objectFactory.getObject();
    objectInput.setState(STATE.OFFERED);
    objectInput.setOfferDate(LocalDateTime.now());

    if (json.get("ownerId").asInt() != 0) {
      objectInput.setOwnerId(json.get("ownerId").asInt());
    } else {
      objectInput.setOwnerId(0);
    }

    String name = json.get("name").asText();
    String description = json.get("description").asText();
    int typeObjectId = json.get("type_object_id").asInt();
    int availabilityId = json.get("availability_id").asInt();
    String phoneOwner = json.hasNonNull("phone_owner") ? json.get("phone_owner").asText() : null;

    objectInput.setTypeObject(typeObjectId);
    objectInput.setName(name);
    objectInput.setPicturePath(null);
    objectInput.setAvailabilityId(availabilityId);
    objectInput.setPhoneOwner(phoneOwner);
    objectInput.setDescription(description);

    ObjectDto objectReturn = objectUCC.createObject(objectInput);
    notificationUCC.createNotificationForNewObject(
        objectReturn);

    return objectReturn;
  }

  /**
   * GET route used to get all objects of the users connected.
   *
   * @param request given by grizzly
   * @return list with all objects of the user
   */
  @GET
  @Path("myObjects")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ObjectDto> myObjects(@Context ContainerRequest request) {
    MyLogger.info(" The user is in objects/myObjects route");

    // GET INPUT
    UserDto user = (UserDto) request.getProperty("user");

    return objectUCC.getAllObjectsFromOneUser(user.getId());
  }

  /**
   * GET route used to get all the data of an object of a user.
   *
   * @param id the ID of the user
   * @return JSON object with all its data
   */
  @GET
  @Path("getObjectsFromUser")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDto> getObjectsFromUser(@QueryParam("id") String id) {
    MyLogger.info(" The user is in objects/getObjectsFromUser route");

    // CHECK VALID ID
    if (id == null || id.isBlank()) {
      MyLogger.warning("Invalid ID provided in getObjectsFromUser request");
      throw new BadRequestException("Invalid ID provided in getObjectsFromUser request");
    }

    // GET INPUT
    int userId = Integer.parseInt(id);

    return objectUCC.getAllObjectsFromOneUser(userId);
  }

  /**
   * Retrieves a list of all objects currently offered.
   *
   * @return A list of all objects in the offered state.
   */
  @GET
  @Path("getAllObjectsInStore")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDto> getAllObjectsInStore() {
    MyLogger.info(" The user is in objects/getAllObjectsInStore route");

    return objectUCC.getAllObjects().stream().filter(e -> e.getState().equals(STATE.STORE))
        .collect(
            Collectors.toList());
  }


  /**
   * GET route used to get all the existing types of object.
   *
   * @return a list of all the objects types
   */
  @GET
  @Path("getAllObjectsType")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<TypeObjectDto> getAllObjectsType() {
    MyLogger.info(" The user is in objects/getAllObjectsType route");

    return objectUCC.getAllTypeObjects();
  }

  /**
   * GET route used to get all the existing availibilities.
   *
   * @return a list of all the objects types
   */
  @GET
  @Path("getAllAvailibilities")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<AvailabilityDto> getAllAvailibilities() {
    MyLogger.info(" The user is in objects/getAllAvailibilities route");

    return availabilityUCC.getAllAvailabilities();
  }


  /**
   * Retrieves a list of all objects currently in store or sold.
   *
   * @return A list of all objects in the store or sold state.
   */
  @GET
  @Path("getAllObjectsInStoreOrSold")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDto> getAllObjectsInStoreOrSold() {
    MyLogger.info(" The user is in objects/getAllObjectsInStoreOrSold route");

    return objectUCC.getAllObjects().stream()
        .filter(e -> e.getState().equals(STATE.STORE) || e.getState().equals(STATE.SOLD)
            || e.getState().equals(STATE.SALE)).collect(
            Collectors.toList());
  }

  /**
   * GET route used to search objects for a specific recovery date.
   *
   * @param date the searched date
   * @return JSON object with all its data
   */
  @GET
  @Path("getObjectsForRecoveryDate")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDto> getObjectsForRecoveryDate(@QueryParam("date") String date) {
    MyLogger.info(" The user is in objects/getObjectsForRecoveryDate route");

    // CHECK VALID DATE
    if (date == null || date.isBlank()) {
      MyLogger.warning("Invalid date provided in getObjectsForRecoveryDate request");
      throw new BadRequestException("Invalid date provided in getObjectsForRecoveryDate request");
    }

    if (!date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{6}")) {
      MyLogger.warning("Bad date format provided in getObjectsForRecoveryDate request");
      throw new BadRequestException(
          "Bad date format provided in getObjectsForRecoveryDate request");
    }

    LocalDateTime dateTime = LocalDateTime.parse(date);

    return objectUCC.getObjectsForRecoveryDate(dateTime);
  }

  /**
   * GET route used to search objects filter by object types.
   *
   * @param types the types of objects to filter (id)
   * @return JSON object with all its data
   */
  @GET
  @Path("getObjectsSortedByTypes")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDto> getObjectsSortedByTypes(@QueryParam("types") List<Integer> types) {
    MyLogger.info(" The user is in objects/getObjectsSortedByTypes route");

    // CHECK VALID DATE
    if (types == null || types.isEmpty()) {
      MyLogger.warning("No types found");
      throw new BadRequestException("No types found");
    }

    return objectUCC.getObjectsSortedByTypes(types);
  }

  /**
   * GET route used to search objects between two price.
   *
   * @param priceMin the price minimum to include.
   * @param priceMax the price maximum to include.
   * @return JSON object with all its data
   */
  @GET
  @Path("getObjectsBetweenTwoPrice")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjectDto> getObjectsBetweenTwoPrice(@QueryParam("priceMin") int priceMin,
      @QueryParam("priceMax") int priceMax) {
    if (priceMin < 0 || priceMax < priceMin) {
      throw new BadRequestException("No prices found");
    }

    return objectUCC.getObjectsBetweenTwoPrice(priceMin, priceMax);
  }

  /**
   * GET number of sold objects (stats).
   *
   * @return number of sold objects
   */
  @GET
  @Path("getNumberOfSoldObjects")
  @Produces(MediaType.APPLICATION_JSON)
  public int getNumberOfSoldObjects() {

    return objectUCC.getNumberOfSoldObjects();
  }

  /**
   * GET number of proposed objects by periode.
   *
   * @return a map : Year, number of objects
   */
  @GET
  @Path("getNumberOfProposedObjectsByPeriod")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, Integer> getNumberOfProposedObjectsByPeriod() {

    return objectUCC.getNumberOfProposedObjectsByPeriod();
  }

  /**
   * POST route used to search objects between two dates.
   *
   * @param json the received infos from front-end
   * @return JSON object with all its data
   */
  @POST
  @Path("getObjectsBetweenTwoDate")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public List<ObjectDto> getObjectsBetweenTwoDate(JsonNode json) {
    String startDate = json.get("startDate").asText();
    String endDate = json.get("endDate").asText();

    LocalDateTime localDateTimeStartDate = LocalDateTime.parse(startDate);
    LocalDateTime localDateTimeEndDate = LocalDateTime.parse(endDate);

    if (localDateTimeEndDate.isBefore(localDateTimeStartDate)) {
      throw new BadRequestException("End Date is Before Start Date");
    }

    return objectUCC.getObjectsBetweenTwoDate(localDateTimeStartDate, localDateTimeEndDate);
  }

  /**
   * POST route used to update information on an object.
   *
   * @param json the received infos from front-end.
   * @return JSON object with all its data
   */
  @POST
  @Path("updateObject")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public ObjectDto updateObject(JsonNode json) {
    MyLogger.info(" The user is in objects/updateObject route");
    int objectId = json.get("id").asInt();
    ObjectDto objectDto = objectUCC.getOneObjectById(objectId);
    System.out.println("description : " + json.get("description").asText());
    if (json.hasNonNull("description") && !json.get("description").asText().isBlank()) {
      objectDto.setDescription(json.get("description").asText());
    }

    if (json.hasNonNull("type") && !json.get("type").asText().isBlank()) {
      objectDto.setTypeObject(json.get("type").asInt());
    }

    return objectUCC.updateObject(objectDto);
  }


  /**
   * POST route used to update the object state to withdraw state.
   *
   * @param json JSON object received from Grizzly
   * @return JSON object with the new state
   */
  @POST
  @Path("withdraw")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public ObjectDto withdrawObject(JsonNode json) {
    MyLogger.info(" The user is in objects/withdraw route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in withdrawObject request");
      throw new BadRequestException("Invalid ID provided in withdrawObject request");
    }

    // GET INPUT
    int objectId = Integer.parseInt(json.get("id").asText());

    return objectUCC.withdrawnObjectState(objectId);
  }
}