package be.vinci.pae.services.strategies;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * class used for the READ set of operation of the user. this one read based on email.
 */
public class GetByEmail extends GetByStrategy<String> {

  /**
   * Constructor.
   *
   * @param email the email of the user.
   */
  public GetByEmail(String email) {
    super(email, "email");
  }

  @Override
  public void setParam(PreparedStatement ps) throws SQLException {
    ps.setString(1, super.getInput());
  }
}
