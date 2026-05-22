package se.iths.fabian.productservicemajprojekt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.iths.fabian.productservicemajprojekt.dto.ProductRequestDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductResponseDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductStockRequest;
import se.iths.fabian.productservicemajprojekt.entity.Product;
import se.iths.fabian.productservicemajprojekt.exception.InsufficientStockException;
import se.iths.fabian.productservicemajprojekt.exception.ProductNotFoundException;
import se.iths.fabian.productservicemajprojekt.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = mapToEntity(requestDto);
        Product savedProduct = productRepository.save(product);
        return mapToResponseDto(savedProduct);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        return mapToResponseDto(findProductOrThrow(id));
    }

    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    @Transactional
    public List<ProductResponseDto> decreaseStock(List<ProductStockRequest> requests) {
        return requests.stream().map(request -> {
            Product product = findProductOrThrow(request.getProductId());

            if (product.getQuantity() < request.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - request.getQuantity());
            Product updatedProduct = productRepository.save(product);
            return mapToResponseDto(updatedProduct);
        }).collect(Collectors.toList());
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    private Product mapToEntity(ProductRequestDto dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
