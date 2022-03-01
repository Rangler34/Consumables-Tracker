package ca.cmpt213.a4.client.control;

import com.google.gson.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages the consumables
 */
public class ConsumableManager {


    private static final ArrayList<Consumable> consumables = new ArrayList<>();
    static ConsumableFactory consumableFactory = ConsumableFactory.getInstance();


    /**
     * Gets the consumable array list
     *
     * @return array list
     */
    public ArrayList<Consumable> getConsumables() {
        return consumables;
    }


    /**
     * Gets the consumable array list from the server
     *
     * @throws IOException
     */
    //https://zetcode.com/java/getpostrequest/
    //https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
    public void getConsumablesFromServer() throws IOException {
        URL url = new URL("http://localhost:8080/listAll");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();


        JsonElement fileValue = JsonParser.parseString(inline.toString());
        JsonArray jsonList = fileValue.getAsJsonArray();

        for (int i = 0; i < jsonList.size(); i++) {

            JsonObject taskObj = jsonList.get(i).getAsJsonObject();
            int type = taskObj.get("type").getAsInt();
            String name = taskObj.get("name").getAsString();
            String notes = taskObj.get("notes").getAsString();
            double price = taskObj.get("price").getAsDouble();
            double size = taskObj.get("size").getAsDouble();
            String taskDate = taskObj.get("expiryDate").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime expiry = LocalDateTime.parse(taskDate, formatter);

            Consumable consumable = consumableFactory.getConsumable(type, name, notes, price, size, expiry);
            consumables.add(consumable);

        }
    }


    /**
     * Adds a consumable to the server
     *
     * @param type       item type 1 for food 2 for drink
     * @param consumable the consumable as a json
     * @throws IOException
     */
    //https://zetcode.com/java/getpostrequest/
    public void addConsumableToServer(int type, String consumable) throws IOException {

        if (type == 1) {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(("http://localhost:8080/addFood")))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(consumable))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(("http://localhost:8080/addDrink")))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(consumable))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * Saves the items in the array list and passes to the server
     *
     * @throws IOException
     */
    //https://zetcode.com/java/getpostrequest/
    public void saveItemsInServer() throws IOException {

        URL url = new URL("http://localhost:8080/exit");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int response = connection.getResponseCode();
        if (response != 200) {
            throw new RuntimeException("Response was" + response);
        }

        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            builder.append(scanner.nextLine());
        }
        scanner.close();

    }


    /**
     * removes an item in the arraylist
     *
     * @param pos the position of the item wanting to be removed
     * @throws IOException
     */
    //https://zetcode.com/java/getpostrequest/
    public void removeItem(int pos) throws IOException {

        pos = pos - 1;
        if (pos < 0 || consumables.size() == 0) {
            return;
        } else {
            URL url = new URL("http://localhost:8080/removeItem/" + pos);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.valueOf(url)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(pos)))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}


