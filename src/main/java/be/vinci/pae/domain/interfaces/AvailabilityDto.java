package be.vinci.pae.domain.interfaces;

import java.time.LocalDateTime;

/**
 * This interface describe the data transfer object for the availabilitys.
 */
public interface AvailabilityDto {

  /**
   * get the id.
   *
   * @return the id.
   */
  int getId();

  /**
   * set the id.
   *
   * @param id the id.
   */
  void setId(int id);

  /**
   * get the date.
   *
   * @return the date.
   */
  LocalDateTime getAvailability();

  /**
   * set the date.
   *
   * @param availability the date
   */
  void setAvailability(LocalDateTime availability);

  /**
   * get the time of the day for the date.
   *
   * @return the time of the day.
   */
  String getTimeSlot();

  /**
   * set the time of the day for the date.
   *
   * @param timeSlot the time of the day.
   */
  void setTimeSlot(String timeSlot);
}
