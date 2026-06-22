package github.com.diegogrlima.gocoffe.infrastructure.persistence.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    Optional<ProductJpaEntity> findByName(String name);

    Page<ProductJpaEntity> findAll(Pageable pageable);

    List<ProductJpaEntity> findByCategoryId(UUID categoryId);
}
