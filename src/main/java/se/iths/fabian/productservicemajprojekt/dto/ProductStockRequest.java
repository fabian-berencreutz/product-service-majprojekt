package se.iths.fabian.productservicemajprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequest {
    private Long productId;
    private int quantity;
}
