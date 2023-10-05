package be.vinci.pae.domain.interfaces;

import jakarta.ws.rs.NotAllowedException;

/**
 * This interface describe the business object for the Objects.
 */
public interface Object extends ObjectDto {

  /**
   * Check that the object can be set to CONFIRMED, and set it if it can.
   */
  void setObjectStateConfirmed() throws NotAllowedException;

  /**
   * Check that the object can be set to DENIED, and set it if it can.
   *
   * @param refusalReason the reason why the object was refused.
   * @throws NotAllowedException if the object is in not in the required state.
   */
  void setObjectStateDenied(String refusalReason) throws NotAllowedException;

  /**
   * Check that the object can be set to WORKSHOP, and set it if it can.
   *
   * @throws NotAllowedException if the object is in not in the required state.
   */
  void setObjectStateWorkshop() throws NotAllowedException;

  /**
   * Check that the object can be set to STORE, and set it if it can.
   *
   * @throws NotAllowedException if the object is in not in the required state.
   */
  void setObjectStateStore() throws NotAllowedException;

  /**
   * Check that the object can be set to SALE, and set it if it can.
   *
   * @param sellingPrince the price set for selling the object.
   * @throws NotAllowedException if the object is in not in the required state, OR if the price is
   *                             not strictly positive.
   */
  void setObjectStateSale(double sellingPrince) throws NotAllowedException;

  /**
   * Check that the object can be set to SOLD, and set it if it can.
   *
   * @throws NotAllowedException if the object is in not in the required state.
   */
  void setObjectStateSold() throws NotAllowedException;

  /**
   * Set the object to sold when it is in Store state.
   *
   * @param sellingPrice price that the object has been sold for.
   * @throws NotAllowedException if the object is in not in the required state.
   */
  void setObjectStateSoldWithPrice(double sellingPrice) throws NotAllowedException;

  /**
   * Check that the object can be set to WITHDRAWN, and set it if it can.
   *
   * @throws NotAllowedException if the object is in not in the required state.
   */
  void setObjectStateWithdrawn() throws NotAllowedException;

  /**
   * Represents the existing states of an object.
   */
  enum STATE {
    /**
     * when an object is offered.
     */
    OFFERED,
    /**
     * when an object has been accepted.
     */
    CONFIRMED,
    /**
     * when an object has been refused.
     */
    DENIED,
    /**
     * when an object is in the workshop.
     */
    WORKSHOP,
    /**
     * when an object is in the store.
     */
    STORE,
    /**
     * when an object is in sale.
     */
    SALE,
    /**
     * when an object has been sold.
     */
    SOLD,
    /**
     * when an object has been removed (ie: when an object has been on SALE for too long).
     */
    WITHDRAWN
  }

}