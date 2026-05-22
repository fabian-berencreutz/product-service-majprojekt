package se.iths.fabian.productservicemajprojekt.mapper;

import org.mapstruct.Mapper;
import se.iths.fabian.productservicemajprojekt.dto.ProductRequestDto;
import se.iths.fabian.productservicemajprojekt.dto.ProductResponseDto;
import se.iths.fabian.productservicemajprojekt.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);
    Product toEntity(ProductRequestDto dto);
}
