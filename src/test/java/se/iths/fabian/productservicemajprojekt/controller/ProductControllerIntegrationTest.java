package se.iths.fabian.productservicemajprojekt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.fabian.productservicemajprojekt.dto.ProductRequestDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductStockRequest;
import se.iths.fabian.productservicemajprojekt.entity.Product;
import se.iths.fabian.productservicemajprojekt.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void createProduct_withAdmin_shouldReturnCreated() throws Exception {
        ProductRequestDto request = ProductRequestDto.builder()
                .name("Laptop")
                .description("Powerful gaming laptop")
                .price(new BigDecimal("15000.00"))
                .quantity(5)
                .build();

        mockMvc.perform(post("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void createProduct_withUser_shouldReturnForbidden() throws Exception {
        ProductRequestDto request = ProductRequestDto.builder()
                .name("Laptop")
                .price(new BigDecimal("15000"))
                .build();

        mockMvc.perform(post("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllProducts_withUser_shouldReturnList() throws Exception {
        productRepository.save(new Product(null, "Mouse", "Wireless", new BigDecimal("500"), 10));

        mockMvc.perform(get("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getProductById_withUser_shouldReturnProduct() throws Exception {
        Product saved = productRepository.save(new Product(null, "Screen", "4K", new BigDecimal("4000"), 3));

        mockMvc.perform(get("/products/" + saved.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Screen"));
    }

    @Test
    void deleteProduct_withAdmin_shouldReturnNoContent() throws Exception {
        Product saved = productRepository.save(new Product(null, "Old Item", "To be deleted", new BigDecimal("10"), 1));

        mockMvc.perform(delete("/products/" + saved.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());

        assert !productRepository.existsById(saved.getId());
    }

    @Test
    void deleteProduct_withUser_shouldReturnForbidden() throws Exception {
        Product saved = productRepository.save(new Product(null, "Old Item", "To be deleted", new BigDecimal("10"), 1));

        mockMvc.perform(delete("/products/" + saved.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void decreaseStock_withUser_shouldUpdateStock() throws Exception {
        Product p1 = productRepository.save(new Product(null, "P1", "D1", new BigDecimal("100"), 10));

        List<ProductStockRequest> requests = List.of(new ProductStockRequest(p1.getId(), 2));

        mockMvc.perform(post("/products/stock/decrease")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    void decreaseStock_withInsufficientStock_shouldReturnBadRequest() throws Exception {
        Product p1 = productRepository.save(new Product(null, "P1", "D1", new BigDecimal("100"), 5));

        List<ProductStockRequest> requests = List.of(new ProductStockRequest(p1.getId(), 10));

        mockMvc.perform(post("/products/stock/decrease")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isBadRequest());
    }
}
