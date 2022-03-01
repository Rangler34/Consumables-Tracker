package ca.cmpt213.a4.webappserver.model;


/**
 * Creates an instance of a food or drink object
 */
public class ConsumableFactory {

    private static ConsumableFactory instance;
    public int selectionType;

    /**
     * used to call consumable factory everywhere
     */
    private ConsumableFactory() {

    }

    /**
     * Gets the instance of the consumable factory
     *
     * @return the instance
     */
    public static ConsumableFactory getInstance() {
        if (instance == null) {
            instance = new ConsumableFactory();
        }
        return instance;
    }


    /**
     * gets consumable and calls constructor either food or drink
     *
     * @param type   1 or 2
     * @param name   name of item
     * @param notes  notes of item
     * @param price  price of item
     * @param size   weight or volume of item
     * @param expiry expiry date of item
     * @return food constructor or drink constructor
     */
    public Consumable getConsumable(int type, String name, String notes, double price, double size, String expiry) {

        if (type == 1) {
            selectionType = 1;
            return new FoodInfo(type, name, notes, price, size, expiry);
        } else if (type == 2) {
            selectionType = 2;
            return new DrinkInfo(type, name, notes, price, size, expiry);
        }
        return null;
    }

}
