package ca.cmpt213.a4.client.control;

/**
 * consumable interface stores methods to be used
 */
public interface Consumable extends Comparable<Consumable> {

    /**
     * Gets the days until the item will expire
     *
     * @return the days until expiry
     */
    long daysUntilExpiry();

    /**
     * Checks the type of the item
     *
     * @return the item type (1 or 2)
     */
    int checkType();

}