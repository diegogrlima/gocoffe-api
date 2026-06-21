package github.com.diegogrlima.gocoffe.infrastructure.repository.product;

import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
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
import java.util.stream.Collectors;

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
    public List<ProductImage> findByProductId(UUID productId) {
        return productImageJpaRepository.findByProductId(productId).stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        productImageJpaRepository.deleteById(id);
    }

    private ProductImageJpaEntity toJpaEntity(ProductImage productImage) {
        ProductJpaEntity productJpa = ProductJpaEntity.builder()
                .id(productImage.getProduct().getId())
                .build();

        return ProductImageJpaEntity.builder()
                .id(productImage.getId())
                .fileName(productImage.getFileName())
                .contentType(productImage.getContentType())
                .data(productImage.getData())
                .product(productJpa)
                .build();
    }

    private ProductImage toDomainEntity(ProductImageJpaEntity jpaEntity) {
        Product product = Product.builder()
                .id(jpaEntity.getProduct().getId())
                .name(jpaEntity.getProduct().getName())
                .build();

        return ProductImage.builder()
                .id(jpaEntity.getId())
                .fileName(jpaEntity.getFileName())
                .contentType(jpaEntity.getContentType())
                .data(jpaEntity.getData())
                .product(product)
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
