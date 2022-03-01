package ca.cmpt213.a4.webappserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String expiryDate;
    private int type;

    /**
     * gets the name of the item
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * get the notes of the item
     *
     * @return the item notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * gets the price of the item
     *
     * @return the items price
     */
    public double getPrice() {
        return price;
    }

    /**
     * gets the size of the item (weight)
     *
     * @return the items size
     */
    public double getSize() {
        return size;
    }

    /**
     * gets the expiry date of the item
     *
     * @return the item's expiry date
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * gets the type of the item
     *
     * @return the items type
     */
    public int getType() {
        return type;
    }

    /**
     * Constructor stores the information of the food
     *
     * @param name       of the food item
     * @param notes      of the food item
     * @param price      of the food item
     * @param size       of the food item
     * @param expiryDate of the food item
     */
    public FoodInfo(@JsonProperty("type") int type, @JsonProperty("name") String name, @JsonProperty("notes") String notes,
                    @JsonProperty("price") double price, @JsonProperty("size") double size, @JsonProperty("expiryDate") String expiryDate) {

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
        //https://www.java67.com/2016/04/how-to-convert-string-to-localdatetime-in-java8-example.html
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(expiryDate, formatter);

        LocalDateTime currentTime = LocalDateTime.now();
        return DAYS.between(currentTime.toLocalDate(), dateTime.toLocalDate());
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(expiryDate, formatter);
        DateTimeFormatter formatterTwo = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateTimeString = dateTime.format(formatterTwo);


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
