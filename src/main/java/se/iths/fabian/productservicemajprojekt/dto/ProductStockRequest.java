package se.iths.fabian.productservicemajprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) som specificerar en begäran om lagerändring för en specifik produkt.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequest {
    private Long productId;
    private int quantity;
}
