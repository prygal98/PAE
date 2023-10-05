package be.vinci.pae.domain.entity;

import be.vinci.pae.domain.interfaces.AvailabilityDto;
import be.vinci.pae.domain.interfaces.AvailabilityFactory;

/**
 * This class is used to create availability objects.
 */
public class AvailabilityFactoryImpl implements AvailabilityFactory {

  public AvailabilityDto getAvailability() {
    return new AvailabilityImpl();
  }

}
