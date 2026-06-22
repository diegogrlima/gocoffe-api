package github.com.diegogrlima.gocoffe.infrastructure.persistence.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    Optional<ProductJpaEntity> findByName(String name);

    List<ProductJpaEntity> findByCategoryId(UUID categoryId);
}
