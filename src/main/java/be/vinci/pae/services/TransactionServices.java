package be.vinci.pae.services;

/**
 * This interface contains the methods to make transactions.
 */
public interface TransactionServices {

  /**
   * Start a transaction.
   */
  void start();

  /**
   * Commit a transaction to apply changes.
   */
  void commit();

  /**
   * Rollback a transaction, to undo changes.
   */
  void rollback();
}
