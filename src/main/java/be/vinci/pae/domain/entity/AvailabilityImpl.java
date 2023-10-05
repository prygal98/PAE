package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.Availability;
import be.vinci.pae.domain.interfaces.AvailabilityDto;
import java.time.LocalDateTime;
import java.util.Date;

class AvailabilityImpl implements Availability, AvailabilityDto {

  private int id;
  private LocalDateTime availability;
  private String timeSlot;

  public AvailabilityImpl() {

  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public LocalDateTime getAvailability() {
    return availability;
  }

  @Override
  public void setAvailability(LocalDateTime availability) {
    this.availability = availability;
  }

  @Override
  public String getTimeSlot() {
    return timeSlot;
  }

  @Override
  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  public boolean checkDateValidity(Date date) {
    return true;
  }
}
