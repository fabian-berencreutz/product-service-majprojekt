package se.iths.fabian.productservicemajprojekt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.iths.fabian.productservicemajprojekt.dto.ProductRequestDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductResponseDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductStockRequest;
import se.iths.fabian.productservicemajprojekt.service.ProductService;

import java.util.List;

/**
 * REST-controller som exponerar HTTP-endpoints för CRUD-operationer på produkter
 * samt funktioner för att minska och öka lagersaldon.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Skapar en ny produkt.
     *
     * @param requestDto förfrågans body med uppgifter om den nya produkten
     * @return den skapade produkten som {@link ProductResponseDto} med HTTP-status 201 Created
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
        return productService.createProduct(requestDto);
    }

    /**
     * Hämtar en lista över alla tillgängliga produkter.
     *
     * @return en lista med alla produkter som {@link ProductResponseDto}
     */
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Hämtar en specifik produkt baserat på dess ID.
     *
     * @param id produktens ID
     * @return den efterfrågade produkten som {@link ProductResponseDto}
     */
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * Raderar en specifik produkt baserat på dess ID.
     *
     * @param id produktens ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    /**
     * Minskar lagersaldot för angivna produkter, exempelvis i samband med en beställning.
     *
     * @param requests lista med produkter och kvantiteter som ska dras från lagret
     * @return en lista med uppdaterade produkter
     */
    @PostMapping("/stock/decrease")
    public List<ProductResponseDto> decreaseStock(@RequestBody List<ProductStockRequest> requests) {
        return productService.decreaseStock(requests);
    }

    /**
     * Ökar lagersaldot för angivna produkter, exempelvis vid varupåfyllning eller avbruten order.
     *
     * @param requests lista med produkter och kvantiteter som ska läggas till lagret
     * @return en lista med uppdaterade produkter
     */
    @PostMapping("/stock/increase")
    public List<ProductResponseDto> increaseStock(@RequestBody List<ProductStockRequest> requests) {
        return productService.increaseStock(requests);
    }
}
