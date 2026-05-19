package se.iths.fabian.productservicemajprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.fabian.productservicemajprojekt.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
