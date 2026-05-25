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
import se.iths.fabian.productservicemajprojekt.mapper.ProductMapper;
import se.iths.fabian.productservicemajprojekt.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        return productMapper.toResponseDto(findProductOrThrow(id));
    }

    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    @Transactional
    public List<ProductResponseDto> decreaseStock(List<ProductStockRequest> requests) {
        return requests.stream().map(request -> {
            Product product = findProductOrThrow(request.getProductId());

            if (product.getStock() < request.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - request.getQuantity());
            productRepository.save(product);

            ProductResponseDto response = productMapper.toResponseDto(product);
            response.setQuantity(request.getQuantity());
            return response;
        }).collect(Collectors.toList());
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
}
