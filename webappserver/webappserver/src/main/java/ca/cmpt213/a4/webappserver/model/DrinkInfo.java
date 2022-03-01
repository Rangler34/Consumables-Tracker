package ca.cmpt213.a4.webappserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String expiryDate;
    private int type;

    /**
     * gets the name of the drink
     *
     * @return the name of the drink
     */
    public String getName() {
        return name;
    }

    /**
     * gets the notes of the drink
     *
     * @return the notes of the drink
     */
    public String getNotes() {
        return notes;
    }

    /**
     * get the price of the drink
     *
     * @return the drink price
     */
    public double getPrice() {
        return price;
    }

    /**
     * gets the size of the drink (volume)
     *
     * @return the drink size
     */
    public double getSize() {
        return size;
    }

    /**
     * get the expiry date of the drink
     *
     * @return the drink expiry date
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * get the type of the item
     *
     * @return the item type
     */
    public int getType() {
        return type;
    }

    /**
     * stores the information of the drink
     *
     * @param name       of the drink
     * @param notes      of the drink
     * @param price      of the drink
     * @param size       volume of the drink
     * @param expiryDate expiry date of the drink
     */
    public DrinkInfo(@JsonProperty("type") int type, @JsonProperty("name") String name, @JsonProperty("notes") String notes,
                     @JsonProperty("price") double price, @JsonProperty("size") double size, @JsonProperty("expiryDate") String expiryDate) {

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(expiryDate, formatter);
        return DAYS.between(currentTime.toLocalDate(), dateTime.toLocalDate());
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(expiryDate, formatter);
        DateTimeFormatter formatterTwo = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateTimeString = dateTime.format(formatterTwo);


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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(expiryDate, formatter);
        long daysLeftUntilExpired = DAYS.between(todayTime.toLocalDate(), dateTime.toLocalDate());
        if (daysLeftUntilExpired < o.daysUntilExpiry()) {
            return -1;
        } else {
            return 1;
        }
    }

}

