package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.Responsible;
import be.vinci.pae.api.filters.ResponsibleOrHelper;
import be.vinci.pae.domain.interfaces.NotificationDto;
import be.vinci.pae.domain.interfaces.NotificationUCC;
import be.vinci.pae.utils.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * Annotation to use "/notification" path.
 */
@Singleton
@Path("/notification")
public class NotificationRessource {

  @Inject
  private NotificationUCC notificationUCC;


  /**
   * GET route used to get all the notifications of the proposed.
   *
   * @param request given by grizzly
   * @return a list of notificationsDTO
   */
  @GET
  @Path("getAllNotificationsProposed")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public List<NotificationDto> getAllNotifications(@Context ContainerRequest request) {
    MyLogger.info(" The user is in notification/getAllNotificationsProposed route");

    return notificationUCC.getProposedObjectNotifications();
  }

  /**
   * GET route used to get all the notifications of the proposed.
   *
   * @param id of user owner of notifs
   * @return a list of notificationsDTO
   */
  @GET
  @Path("getAllMyNotifications")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<NotificationDto> getAllMyNotifications(@QueryParam("id") String id) {
    MyLogger.info(" The user is in notification/getAllNotificationsProposed route");
    return notificationUCC.getAllMyNotifications(Integer.parseInt(id));
  }

  /**
   * POST route used to mark a notification as read.
   *
   * @param json the received info from front end.
   * @return a list of notificationsDTO
   */
  @POST
  @Path("markAsRead")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Responsible
  public NotificationDto markAsRead(JsonNode json) {
    MyLogger.info(" The user is in notification/markAsRead route");

    // CHECK VALID ID
    if (!json.hasNonNull("id") || json.get("id").asText().isBlank()) {
      MyLogger.warning("Invalid ID provided in sold request");
      throw new BadRequestException("Invalid ID provided in sold request");
    }

    // GET INPUT
    int notificationId = Integer.parseInt(json.get("id").asText());

    return notificationUCC.markAsRead(notificationId);
  }


}