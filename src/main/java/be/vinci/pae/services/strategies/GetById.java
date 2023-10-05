package be.vinci.pae.services.strategies;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * class used for the READ set of operation of the user. this one read based on id.
 */
public class GetById extends GetByStrategy<Integer> {

  /**
   * Constructor.
   *
   * @param id the id of the user.
   */
  public GetById(Integer id) {
    super(id, "id_user");
  }

  @Override
  public void setParam(PreparedStatement ps) throws SQLException {
    ps.setInt(1, super.getInput());
  }
}
