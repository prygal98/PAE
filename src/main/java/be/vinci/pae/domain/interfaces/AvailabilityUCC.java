package be.vinci.pae.domain.interfaces;

import jakarta.ws.rs.NotFoundException;
import java.util.List;

/**
 * Class for use case of Availability object.
 */
public interface AvailabilityUCC {

  /**
   * Add a new availability to the system.
   *
   * @param availability the date
   * @return AvailabilityDTO
   */
  AvailabilityDto addAvailability(AvailabilityDto availability);

  /**
   * Returns a list of all AvailabilityDto availabilities from DB.
   *
   * @return a list of AvailabilityDto object
   * @throws NotFoundException if the availabilities list is empty
   */
  List<AvailabilityDto> getAllAvailabilities();
}
