package be.vinci.pae.utils;

import static org.mockito.Mockito.mock;

import be.vinci.pae.domain.entity.AvailabilityFactoryImpl;
import be.vinci.pae.domain.entity.NotificationFactoryImpl;
import be.vinci.pae.domain.entity.ObjectFactoryImpl;
import be.vinci.pae.domain.entity.TypeObjectFactoryImpl;
import be.vinci.pae.domain.entity.UserFactoryImpl;
import be.vinci.pae.domain.interfaces.AvailabilityFactory;
import be.vinci.pae.domain.interfaces.AvailabilityUCC;
import be.vinci.pae.domain.interfaces.NotificationFactory;
import be.vinci.pae.domain.interfaces.NotificationUCC;
import be.vinci.pae.domain.interfaces.ObjectFactory;
import be.vinci.pae.domain.interfaces.ObjectUCC;
import be.vinci.pae.domain.interfaces.TypeObjectFactory;
import be.vinci.pae.domain.interfaces.UserFactory;
import be.vinci.pae.domain.interfaces.UserUCC;
import be.vinci.pae.domain.ucc.AvailabilityUCCImpl;
import be.vinci.pae.domain.ucc.NotificationUCCImpl;
import be.vinci.pae.domain.ucc.ObjectUCCImpl;
import be.vinci.pae.domain.ucc.UserUCCImpl;
import be.vinci.pae.services.DalServices;
import be.vinci.pae.services.DalServicesImpl;
import be.vinci.pae.services.TransactionServices;
import be.vinci.pae.services.dao.AvailabilityDAO;
import be.vinci.pae.services.dao.AvailabilityDAOImpl;
import be.vinci.pae.services.dao.NotificationDAO;
import be.vinci.pae.services.dao.NotificationDAOImpl;
import be.vinci.pae.services.dao.ObjectDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.UserDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * This class is used to test the implementation of ApplicationBinder.
 */
@Provider
public class TestApplicationBinder extends AbstractBinder {

  /**
   * A provider for binding implementations to their corresponding interfaces in the application.
   */
  @Override
  protected void configure() {
    // USER
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(mock(UserDAOImpl.class)).to(UserDAO.class);

    // OBJECT
    bind(TypeObjectFactoryImpl.class).to(TypeObjectFactory.class).in(Singleton.class);
    bind(ObjectFactoryImpl.class).to(ObjectFactory.class).in(Singleton.class);
    bind(ObjectUCCImpl.class).to(ObjectUCC.class).in(Singleton.class);
    bind(mock(ObjectDAO.class)).to(ObjectDAO.class);

    // AVAILABILITY
    bind(AvailabilityFactoryImpl.class).to(AvailabilityFactory.class).in(Singleton.class);
    bind(AvailabilityUCCImpl.class).to(AvailabilityUCC.class).in(Singleton.class);
    bind(mock(AvailabilityDAOImpl.class)).to(AvailabilityDAO.class);

    // NOTIFICATION
    bind(NotificationFactoryImpl.class).to(NotificationFactory.class).in(Singleton.class);
    bind(NotificationUCCImpl.class).to(NotificationUCC.class).in(Singleton.class);
    bind(mock(NotificationDAOImpl.class)).to(NotificationDAO.class);

    // SERVICES
    bind(mock(DalServicesImpl.class)).to(TransactionServices.class);
    bind(DalServicesImpl.class).to(DalServices.class).in(Singleton.class);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    bind(mapper).to(ObjectMapper.class);
  }

}
