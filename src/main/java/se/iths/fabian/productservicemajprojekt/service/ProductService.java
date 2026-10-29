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

/**
 * Tjänsteklass som tillhandahåller affärslogik för hantering av produkter och lagersaldon.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Skapar och sparar en ny produkt i databasen baserat på angivet DTO.
     *
     * @param requestDto data för den nya produkten
     * @return en {@link ProductResponseDto} som representerar den sparade produkten
     */
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    /**
     * Hämtar samtliga produkter som finns lagrade i systemet.
     *
     * @return en lista med alla produkter som {@link ProductResponseDto}
     */
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Hämtar en specifik produkt baserat på dess unika identifierare.
     *
     * @param id produktens ID
     * @return produkten som {@link ProductResponseDto}
     * @throws ProductNotFoundException om ingen produkt hittas med angivet ID
     */
    public ProductResponseDto getProductById(Long id) {
        return productMapper.toResponseDto(findProductOrThrow(id));
    }

    /**
     * Raderar en specifik produkt från databasen.
     *
     * @param id ID för produkten som ska tas bort
     * @throws ProductNotFoundException om ingen produkt hittas med angivet ID
     */
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    /**
     * Minskar lagersaldot för en lista av produkter i en transaktion.
     *
     * @param requests lista med förfrågningar innehållande produkt-ID och kvantitet som ska dras av
     * @return en lista med uppdaterade produkt-DTO:er med den begärda kvantiteten
     * @throws ProductNotFoundException om en produkt med angivet ID inte existerar
     * @throws InsufficientStockException om det begärda antalet överstiger tillgängligt lagersaldo
     */
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

    /**
     * Ökar lagersaldot för en lista av produkter i en transaktion.
     *
     * @param requests lista med förfrågningar innehållande produkt-ID och kvantitet som ska läggas till
     * @return en lista med uppdaterade produkt-DTO:er
     * @throws IllegalArgumentException om kvantiteten är mindre än eller lika med noll
     * @throws ProductNotFoundException om en produkt med angivet ID inte existerar
     */
    @Transactional
    public List<ProductResponseDto> increaseStock(List<ProductStockRequest> requests) {
        return requests.stream().map(request -> {
            if (request.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            Product product = findProductOrThrow(request.getProductId());

            product.setStock(product.getStock() + request.getQuantity());
            productRepository.save(product);

            return productMapper.toResponseDto(product);
        }).collect(Collectors.toList());
    }

    /**
     * Hjälpmetod för att hämta en produkt från databasen eller kasta undantag om den inte finns.
     *
     * @param id produktens ID
     * @return den hittade {@link Product}-entiteten
     * @throws ProductNotFoundException om ingen produkt hittas med angivet ID
     */
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + "not found."));
    }
}
