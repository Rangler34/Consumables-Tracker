package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.DrinkInfo;
import ca.cmpt213.a4.webappserver.model.FoodInfo;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

/**
 * Handles all the server requests
 */
@RestController
public class ConsumableController {

    ConsumableManager manager = new ConsumableManager();


    /**
     * On /ping command displays system is up message
     *
     * @return system is up message
     */
    @GetMapping("/ping")
    public String getSystemMessage() {
        return "System is up!";
    }


    /**
     * on /listAll command lists all the items in the array list
     *
     * @return the list of all items in the array list
     */
    @GetMapping("/listAll")
    public ArrayList<Consumable> listAll() {
        return manager.listItems();
    }


    /**
     * on /addFood command adds a food item to the array list
     *
     * @param item the food item
     * @return the list of items
     */
    @PostMapping("/addFood")
    public ArrayList<Consumable> addFood(@RequestBody FoodInfo item) {
        manager.addConsumable(item);
        return manager.listItems();
    }


    /**
     * on /addDrink adds a drink item to the array list
     *
     * @param item the drink item
     * @return the list of items
     */
    @PostMapping("/addDrink")
    public ArrayList<Consumable> addDrink(@RequestBody DrinkInfo item) {
        manager.addConsumable(item);
        return manager.listItems();
    }


    /**
     * on /remove/(pos) removes an item in the array list for the pos entered
     *
     * @param pos the position of the item removed
     * @return the list of items
     */
    @PostMapping("/removeItem/{pos}")
    public ArrayList<Consumable> removeItem(@PathVariable("pos") int pos) {
        return manager.removeConsumable(pos);
    }


    /**
     * on /listExpired lists the expired items in the array list
     *
     * @return the list of expired items
     */
    @GetMapping("/listExpired")
    public ArrayList<Consumable> listExpired() {
        return manager.listExpired();
    }


    /**
     * on /listNonExpired lists the non expired items in the array list
     *
     * @return the list of non expired items
     */
    @GetMapping("/listNonExpired")
    public ArrayList<Consumable> listNonExpired() {
        return manager.listNonExpired();
    }


    /**
     * on /listExpiringIn7Days lists the items expiring in 7 days in the array list
     *
     * @return the list of items expiring in 7 days
     */
    @GetMapping("/listExpiringIn7Days")
    public ArrayList<Consumable> listExpiringIn7() {
        return manager.listExpiringIn7();
    }


    /**
     * on /exit saves the items in the array list
     */
    @GetMapping("/exit")
    public void exitSystem() {
        manager.consumableItemsSaveJSON();
    }
}
