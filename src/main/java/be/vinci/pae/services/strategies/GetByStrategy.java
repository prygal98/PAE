package be.vinci.pae.services.strategies;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * class used for the READ set of operation of the user.
 */
public abstract class GetByStrategy<T> {

  /**
   * the input that will be used eg: String email, or Integer id.
   */
  private final T input;
  private final String tableField;

  /**
   * constructor for the strategy.
   *
   * @param tableField name of the field in db
   * @param input      the input (and its type) that will be used for this specific strategy
   */
  public GetByStrategy(T input, String tableField) {
    this.input = input;
    this.tableField = tableField;
  }

  /**
   * Set the param in the PreparedStatement.
   *
   * @param ps : the PreparedStatement to which the param should be added
   * @throws SQLException if the method cannot attach the parameter to the PreparedStatement
   */
  public abstract void setParam(PreparedStatement ps) throws SQLException;

  /**
   * Returns the input send through the constructor. Ex: the user id for ReadId.
   *
   * @return the input send through the constructor.
   */
  T getInput() {
    return input;
  }

  /**
   * Returns the column name as a String.
   *
   * @return the column name as a String
   */
  public String getTableColumnName() {
    return tableField;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetByStrategy<?> that = (GetByStrategy<?>) o;
    return Objects.equals(input, that.input) && Objects.equals(tableField,
        that.tableField);
  }

  @Override
  public int hashCode() {
    return Objects.hash(input, tableField);
  }

}
