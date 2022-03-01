package ca.cmpt213.a4.client.model;

import ca.cmpt213.a4.client.control.Consumable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Stores the information about the food items
 */
public class FoodInfo implements Consumable {

    String name;
    String notes;
    private double price;
    private double size;
    private LocalDateTime expiryDate;
    private int type;


    /**
     * Constructor stores the information of the food
     *
     * @param name       of the food item
     * @param notes      of the food item
     * @param price      of the food item
     * @param size       of the food item
     * @param expiryDate of the food item
     */
    public FoodInfo(int type, String name, String notes, double price, double size, LocalDateTime expiryDate) {

        this.name = name;
        this.notes = notes;
        this.price = price;
        this.size = size;
        this.expiryDate = expiryDate;
        this.type = type;
    }


    /**
     * calculates the days until the item expires
     *
     * @return the days left until item expiry
     */
    @Override
    public long daysUntilExpiry() {
        LocalDateTime currentTime = LocalDateTime.now();
        return DAYS.between(currentTime.toLocalDate(), expiryDate.toLocalDate());
    }


    /**
     * checks the item type (1 for food)
     *
     * @return the type
     */
    @Override
    public int checkType() {
        int selectionType = 1;
        return selectionType;
    }


    /**
     * prints the information of the food
     *
     * @return the information
     */
    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateTimeString = expiryDate.format(formatter);


        return "Name: " + name + "\n" + "Notes: " + notes + "\n" +
                "Price: " + String.format("%.2f", price) + "\n" + "Weight: " + String.format("%.2f", size) + "\n" + "Expiry Date: " + dateTimeString;
    }


    /**
     * compares the day left until expiry to another object
     *
     * @param o other object
     * @return true or false
     */
    @Override
    public int compareTo(Consumable o) {
        LocalDateTime todayTime = LocalDateTime.now();
        long daysLeftUntilExpired = DAYS.between(todayTime.toLocalDate(), expiryDate.toLocalDate());

        if (daysLeftUntilExpired < o.daysUntilExpiry()) {
            return -1;
        } else {
            return 1;
        }

    }

}
