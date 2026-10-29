package se.iths.fabian.productservicemajprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Undantag som kastas när det inte finns tillräckligt med varor i lager för att fullfölja en begäran.
 * Resulterar i HTTP-statuskod 400 (Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

    /**
     * Skapar en ny instans av {@code InsufficientStockException} med ett specificerat felmeddelande.
     *
     * @param message beskrivning av felet
     */
    public InsufficientStockException(String message) {
        super(message);
    }
}
