package se.iths.fabian.productservicemajprojekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Huvudklass och startpunkt för Product Service Spring Boot-applikation.
 */
@SpringBootApplication
public class ProductServiceMajprojektApplication {

    /**
     * Startar applikationen och initierar Spring-kontexten.
     *
     * @param args kommandoradsargument som skickas vid start
     */
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceMajprojektApplication.class, args);
    }

}
