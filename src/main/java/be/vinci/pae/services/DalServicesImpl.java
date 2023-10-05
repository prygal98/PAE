package be.vinci.pae.services;

import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Utility class for database connection.
 */
public class DalServicesImpl implements DalServices, TransactionServices {

  private ThreadLocal<Connection> connectionStorage;
  private BasicDataSource dataSource;


  /**
   * method to start the database.
   */
  public DalServicesImpl() {
    dataSource = new BasicDataSource();
    connectionStorage = new ThreadLocal<>();

    try {
      Class.forName("org.postgresql.Driver");
      System.out.println("Database driver working !");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL missing !");
      System.exit(1);
    }

    dataSource.setUrl(Config.getProperty("DbUri"));
    dataSource.setUsername(Config.getProperty("DbUser"));
    dataSource.setPassword(Config.getProperty("DbPassword"));
    dataSource.setInitialSize(Integer.parseInt(Config.getProperty("DbBaseConnections")));
    dataSource.setMaxTotal(Integer.parseInt(Config.getProperty("DbMaxConnections")));
  }

  @Override
  public PreparedStatement getPs(String query) {
    try {
      return connectionStorage.get().prepareStatement(query);
    } catch (SQLException e) {
      throw new RuntimeException(e); // TODO
    }
  }

  @Override
  public void start() {
    MyLogger.finest("transaction start for thread -> " + Thread.currentThread().getId());
    Connection conn = connectionStorage.get();

    try {
      if (conn == null) {
        conn = dataSource.getConnection();
        connectionStorage.set(conn);
      }
      conn.setAutoCommit(false);
    } catch (SQLException e) {
      throw new RuntimeException(e); // TODO
    }
  }

  @Override
  public void commit() {
    MyLogger.finest("transaction commit for thread -> " + Thread.currentThread().getId());
    Connection conn = connectionStorage.get();

    try {
      conn.commit();
      conn.close();
      connectionStorage.remove();
    } catch (SQLException e) {
      throw new RuntimeException(e); // TODO
    }
  }

  @Override
  public void rollback() {
    MyLogger.finest("transaction rollback for thread -> " + Thread.currentThread().getId());
    Connection conn = connectionStorage.get();

    try {
      conn.rollback();
      conn.close();
      connectionStorage.remove();
    } catch (SQLException e) {
      throw new RuntimeException(e);  // TODO
    }
  }

}
