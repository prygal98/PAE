package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This interface describes the class who give preparedStatements.
 */
public interface DalServices {

  /**
   * Create a prepared statement ready to be executed.
   *
   * @param query : the SQL query used in the database
   * @return a PreparedStatement
   * @throws SQLException if there is a problem
   */
  PreparedStatement getPs(String query) throws SQLException;
}
