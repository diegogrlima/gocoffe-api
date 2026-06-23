package github.com.diegogrlima.gocoffe.infrastructure.repository.product;

import github.com.diegogrlima.gocoffe.domain.product.entity.ProductImage;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductImageRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.product.ProductImageJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.product.ProductImageJpaRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.product.ProductJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public List<ProductImage> findAllByProductId(UUID productId) {
        return productImageJpaRepository.findAllByProductId(productId)
                .stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        productImageJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllByProductId(UUID productId) {
        productImageJpaRepository.deleteAllByProductId(productId);
    }

    private ProductImageJpaEntity toJpaEntity(ProductImage productImage) {
        ProductJpaEntity productJpa = ProductJpaEntity.builder()
                .id(productImage.getProductId())
                .build();

        return ProductImageJpaEntity.builder()
                .id(productImage.getId())
                .imageURL(productImage.getImageURL())
                .product(productJpa)
                .build();
    }

    private ProductImage toDomainEntity(ProductImageJpaEntity jpaEntity) {
        return ProductImage.builder()
                .id(jpaEntity.getId())
                .imageURL(jpaEntity.getImageURL())
                .productId(jpaEntity.getProduct().getId())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
