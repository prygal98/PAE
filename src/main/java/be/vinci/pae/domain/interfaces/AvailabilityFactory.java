package be.vinci.pae.domain.interfaces;

/**
 * This interface is used to give new availability objects.
 */
public interface AvailabilityFactory {

  /**
   * get a AvailabilityDto object.
   *
   * @return AvailabilityDto.
   */
  AvailabilityDto getAvailability();

}
