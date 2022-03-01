package ca.cmpt213.a4.webappserver;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebAppServerApplication {

    /**
     * Where the items are loaded and where the server is run
     */
    public static void main(String[] args) {
        ConsumableManager.consumableItemsLoadJSON();
        SpringApplication.run(WebAppServerApplication.class, args);
    }

}
