package se.iths.fabian.productservicemajprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Undantag som kastas när en efterfrågad produkt inte hittas i databasen.
 * Resulterar i HTTP-statuskod 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    /**
     * Skapar en ny instans av {@code ProductNotFoundException} med ett specificerat felmeddelande.
     *
     * @param message beskrivning av felet
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
