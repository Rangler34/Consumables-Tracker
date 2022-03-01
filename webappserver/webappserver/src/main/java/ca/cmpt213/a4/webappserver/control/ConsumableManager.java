package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.ConsumableFactory;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Manages the consumables
 */
public class ConsumableManager {


    private static final ArrayList<Consumable> consumables = new ArrayList<>();
    static ConsumableFactory consumableFactory = ConsumableFactory.getInstance();


    /**
     * adds a consumable to the server array list
     *
     * @param item the consumable item
     */
    public void addConsumable(Consumable item) {
        consumables.add(item);
    }


    /**
     * remove a consumable from the array list
     *
     * @param pos of the item to be removed
     * @return consumables array list
     */
    public ArrayList<Consumable> removeConsumable(int pos) {
        if (consumables.size() > 0) {
            consumables.remove(pos--);
        } else {
            return null;
        }
        return consumables;
    }


    /**
     * lists the items in the array list
     *
     * @return consumables array list
     */
    public ArrayList<Consumable> listItems() {

        return consumables;
    }


    /**
     * lists the expired items in the array list
     *
     * @return the array list of expired items
     */
    public ArrayList<Consumable> listExpired() {
        ArrayList<Consumable> expiredList = new ArrayList<>();


        int expiredCounter = 0;


        Collections.sort(consumables);
        for (Consumable items : consumables) {
            if (items.daysUntilExpiry() < 0) {
                expiredList.add(items);
                expiredCounter++;
            }
        }

        if (expiredCounter == 0) {
            return null;
        }
        return expiredList;
    }


    /**
     * lists the non expired items
     *
     * @return the array list of items that are non expired
     */
    public ArrayList<Consumable> listNonExpired() {
        ArrayList<Consumable> nonExpiredList = new ArrayList<>();

        int nonExpiredCounter = 0;

        Collections.sort(consumables);
        for (Consumable items : consumables) {

            if (items.daysUntilExpiry() >= 0) {

                nonExpiredList.add(items);

                nonExpiredCounter++;
            }

        }

        if (nonExpiredCounter == 0) {
            return null;
        }

        return nonExpiredList;
    }


    /**
     * lists the items expiring in 7 days
     *
     * @return the array list of items expiring in 7 days
     */
    public ArrayList<Consumable> listExpiringIn7() {

        ArrayList<Consumable> expiringIn7List = new ArrayList<>();

        int expiringIn7Counter = 0;

        Collections.sort(consumables);
        for (Consumable items : consumables) {

            if (items.daysUntilExpiry() <= 7 && items.daysUntilExpiry() >= 0) {
                expiringIn7List.add(items);
                expiringIn7Counter++;
            }
        }

        if (expiringIn7Counter == 0) {
            return null;
        }
        return expiringIn7List;
    }


    /**
     * saves the food list and items stored inside when
     * program is exited
     */
    public void consumableItemsSaveJSON() {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();

        try {
            Writer read = new FileWriter("./jsonPath.json");
            myGson.toJson(consumables, read);
            read.flush();
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads the food list and items stored in it when program
     * is opened
     */
    public static void consumableItemsLoadJSON() {
        File file = new File("./jsonPath.json");
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(file));
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject taskObj = array.get(i).getAsJsonObject();
                String taskDate = taskObj.get("expiryDate").getAsString();

                consumables.add(consumableFactory.getConsumable(
                        taskObj.get("type").getAsInt(),
                        taskObj.get("name").getAsString(),
                        taskObj.get("notes").getAsString(),
                        taskObj.get("price").getAsDouble(),
                        taskObj.get("size").getAsDouble(),
                        taskDate
                ));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
