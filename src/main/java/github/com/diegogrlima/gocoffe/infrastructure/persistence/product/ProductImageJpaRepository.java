package github.com.diegogrlima.gocoffe.infrastructure.persistence.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductImageJpaRepository extends JpaRepository<ProductImageJpaEntity, UUID> {

    List<ProductImageJpaEntity> findByProductId(UUID productId);
}
