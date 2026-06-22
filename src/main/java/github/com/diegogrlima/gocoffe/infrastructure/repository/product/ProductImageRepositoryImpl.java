package github.com.diegogrlima.gocoffe.infrastructure.repository.product;

import github.com.diegogrlima.gocoffe.domain.product.entity.ProductImage;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductImageRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.product.ProductImageJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.product.ProductImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductImageRepositoryImpl implements ProductImageRepository {

    private final ProductImageJpaRepository productImageJpaRepository;

    @Override
    public ProductImage save(ProductImage productImage) {
        ProductImageJpaEntity jpaEntity = toJpaEntity(productImage);
        ProductImageJpaEntity savedEntity = productImageJpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<ProductImage> findById(UUID id) {
        return productImageJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public void deleteById(UUID id) {
        productImageJpaRepository.deleteById(id);
    }

    private ProductImageJpaEntity toJpaEntity(ProductImage productImage) {
        return ProductImageJpaEntity.builder()
                .id(productImage.getId())
                .imageURL(productImage.getImageURL())
                .build();
    }

    private ProductImage toDomainEntity(ProductImageJpaEntity jpaEntity) {
        return ProductImage.builder()
                .id(jpaEntity.getId())
                .imageURL(jpaEntity.getImageURL())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
