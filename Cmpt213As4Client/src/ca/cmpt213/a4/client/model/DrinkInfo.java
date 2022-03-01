package ca.cmpt213.a4.client.model;

import ca.cmpt213.a4.client.control.Consumable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Stores the information about the drink items
 */
public class DrinkInfo implements Consumable {

    String name;
    String notes;
    private double price;
    private double size;
    private LocalDateTime expiryDate;
    private int type;


    /**
     * stores the information of the drink
     *
     * @param name       of the drink
     * @param notes      of the drink
     * @param price      of the drink
     * @param size       volume of the drink
     * @param expiryDate expiry date of the drink
     */
    public DrinkInfo(int type, String name, String notes, double price, double size, LocalDateTime expiryDate) {

        this.name = name;
        this.notes = notes;
        this.price = price;
        this.size = size;
        this.expiryDate = expiryDate;
        this.type = type;

    }

    /**
     * shows days until expiry
     *
     * @return the days until expiry
     */
    @Override
    public long daysUntilExpiry() {
        LocalDateTime currentTime = LocalDateTime.now();
        return DAYS.between(currentTime.toLocalDate(), expiryDate.toLocalDate());
    }


    /**
     * checks the type (2 for drink)
     *
     * @return type
     */
    @Override
    public int checkType() {
        int selectionType = 2;
        return selectionType;
    }

    /**
     * prints out the drink information
     *
     * @return the drink information
     */
    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateTimeString = expiryDate.format(formatter);


        return "Name: " + name + "\n" + "Notes: " + notes + "\n" +
                "Price: " + String.format("%.2f", price) + "\n" + "Volume: " + String.format("%.2f", size) + "\n" + "Expiry Date: " + dateTimeString;
    }


    /**
     * compares the expiry date to other object
     *
     * @param o object
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

