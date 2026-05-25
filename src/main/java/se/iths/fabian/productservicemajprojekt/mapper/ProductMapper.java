package se.iths.fabian.productservicemajprojekt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.iths.fabian.productservicemajprojekt.dto.ProductRequestDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductResponseDto;
import se.iths.fabian.productservicemajprojekt.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "stock", target = "quantity")
    ProductResponseDto toResponseDto(Product product);

    @Mapping(source = "quantity", target = "stock")
    Product toEntity(ProductRequestDto dto);
}
