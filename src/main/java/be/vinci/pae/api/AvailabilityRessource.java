package be.vinci.pae.api;

import be.vinci.pae.api.filters.ResponsibleOrHelper;
import be.vinci.pae.domain.interfaces.AvailabilityDto;
import be.vinci.pae.domain.interfaces.AvailabilityFactory;
import be.vinci.pae.domain.interfaces.AvailabilityUCC;
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
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Annotation to use "/availability" path.
 */
@Singleton
@Path("/availability")
public class AvailabilityRessource {

  @Inject
  private AvailabilityUCC availabilityUCC;

  @Inject
  private AvailabilityFactory availabilityFactory;


  /**
   * POST route to add a new availability.
   *
   * @param json containing the date and time slot of the availability
   * @return the availability that has been added
   */
  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsibleOrHelper
  public AvailabilityDto addAvailability(JsonNode json) {
    MyLogger.info(" The user is in availability/add route");

    // CHECK VALID REQUIRED INPUTS
    if (!json.hasNonNull("date") || !json.hasNonNull("time_slot")
        || json.get("date").asText().isBlank() || json.get("time_slot").asText().isBlank()) {
      MyLogger.warning("Missing date or time_slot in request");
      throw new BadRequestException("Missing date or time_slot in request");
    }

    LocalDateTime date;
    try {
      date = LocalDateTime.parse(json.get("date").asText());
    } catch (DateTimeParseException e) {
      MyLogger.warning("Invalid date format provided in request");
      throw new BadRequestException("Invalid date format provided in request", e);
    }
    String timeSlot = json.get("time_slot").asText();

    AvailabilityDto availability = availabilityFactory.getAvailability();
    availability.setTimeSlot(timeSlot);
    availability.setAvailability(date);
    return availabilityUCC.addAvailability(availability);
  }

  /**
   * GET route used to get all the existing availabilities.
   *
   * @return a list of all the availabilities
   */
  @GET
  @Path("getAvailabilitiesList")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<AvailabilityDto> getAvailabilities() {
    return availabilityUCC.getAllAvailabilities();
  }

}