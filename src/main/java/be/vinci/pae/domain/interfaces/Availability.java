package be.vinci.pae.domain.interfaces;

import java.util.Date;

/**
 * This interface describe Availability objects.
 */
public interface Availability extends AvailabilityDto {

  /**
   * Check if the date is a valid date i.e. not a past date.
   *
   * @param date the date.
   * @return true if the date is valide.
   */
  boolean checkDateValidity(Date date);
}
