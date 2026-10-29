package se.iths.fabian.productservicemajprojekt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.iths.fabian.productservicemajprojekt.dto.ProductRequestDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductResponseDto;
import se.iths.fabian.productservicemajprojekt.entity.Product;

/**
 * MapStruct-mapper för konvertering mellan {@link Product}-entiteten och tillhörande DTO-objekt.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Konverterar en {@link Product}-entitet till ett {@link ProductResponseDto}.
     * Mappar entitetens {@code stock}-fält till DTO:ns {@code quantity}-fält.
     *
     * @param product entiteten som ska konverteras
     * @return motsvarande {@link ProductResponseDto}
     */
    @Mapping(source = "stock", target = "quantity")
    ProductResponseDto toResponseDto(Product product);

    /**
     * Konverterar ett {@link ProductRequestDto} till en {@link Product}-entitet.
     * Mappar DTO:ns {@code quantity}-fält till entitetens {@code stock}-fält.
     *
     * @param dto DTO-objektet som ska konverteras
     * @return motsvarande {@link Product}-entitet
     */
    @Mapping(source = "quantity", target = "stock")
    Product toEntity(ProductRequestDto dto);
}
