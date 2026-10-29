package se.iths.fabian.productservicemajprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.fabian.productservicemajprojekt.entity.Product;

/**
 * Repository-gränssnitt för databasoperationer på {@link Product}-entiteter.
 * Ärver standard CRUD- och pagineringsmetoder från {@link JpaRepository}.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
