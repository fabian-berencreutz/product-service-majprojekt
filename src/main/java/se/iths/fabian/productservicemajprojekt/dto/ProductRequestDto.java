package se.iths.fabian.productservicemajprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.iths.fabian.productservicemajprojekt.entity.Category;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) för att ta emot förfrågningar om att skapa eller uppdatera en produkt.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private Category category;
    private String imageUrl;
}
