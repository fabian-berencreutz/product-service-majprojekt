package se.iths.fabian.productservicemajprojekt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.fabian.productservicemajprojekt.dto.ProductRequestDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductStockRequest;
import se.iths.fabian.productservicemajprojekt.entity.Product;
import se.iths.fabian.productservicemajprojekt.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void createProduct_shouldReturnCreated() throws Exception {
        ProductRequestDto request = ProductRequestDto.builder()
                .name("Laptop")
                .description("Powerful gaming laptop")
                .price(new BigDecimal("15000.00"))
                .quantity(5)
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void getAllProducts_shouldReturnList() throws Exception {
        productRepository.save(new Product(null, "Mouse", "Wireless", new BigDecimal("500"), 10));
        productRepository.save(new Product(null, "Keyboard", "Mechanical", new BigDecimal("1200"), 5));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getProductById_shouldReturnProduct() throws Exception {
        Product saved = productRepository.save(new Product(null, "Screen", "4K", new BigDecimal("4000"), 3));

        mockMvc.perform(get("/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Screen"));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {
        Product saved = productRepository.save(new Product(null, "Old Item", "To be deleted", new BigDecimal("10"), 1));

        mockMvc.perform(delete("/products/" + saved.getId()))
                .andExpect(status().isNoContent());

        assert !productRepository.existsById(saved.getId());
    }

    @Test
    void decreaseStock_shouldUpdateStock() throws Exception {
        Product p1 = productRepository.save(new Product(null, "P1", "D1", new BigDecimal("100"), 10));
        Product p2 = productRepository.save(new Product(null, "P2", "D2", new BigDecimal("200"), 20));

        List<ProductStockRequest> requests = List.of(
                new ProductStockRequest(p1.getId(), 2),
                new ProductStockRequest(p2.getId(), 5)
        );

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[1].quantity").value(5));
    }

    @Test
    void decreaseStock_whenInsufficient_shouldReturnBadRequest() throws Exception {
        Product p = productRepository.save(new Product(null, "Limited", "Only 1 left", new BigDecimal("100"), 1));

        List<ProductStockRequest> requests = List.of(new ProductStockRequest(p.getId(), 5));

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isBadRequest());
    }
}
