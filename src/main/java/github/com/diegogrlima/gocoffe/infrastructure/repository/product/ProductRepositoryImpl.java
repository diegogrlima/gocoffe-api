package github.com.diegogrlima.gocoffe.infrastructure.repository.product;

import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.category.CategoryJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.product.ProductJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.product.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        ProductJpaEntity jpaEntity = toJpaEntity(product);
        ProductJpaEntity savedEntity = productJpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryId(UUID categoryId) {
        return productJpaRepository.findByCategoryId(categoryId).stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        productJpaRepository.deleteById(id);
    }

    private ProductJpaEntity toJpaEntity(Product product) {
        CategoryJpaEntity categoryJpa = CategoryJpaEntity.builder()
                .id(product.getCategory().getId())
                .name(product.getCategory().getName())
                .build();

        return ProductJpaEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .available(product.getAvailable())
                .category(categoryJpa)
                .build();
    }

    private Product toDomainEntity(ProductJpaEntity jpaEntity) {
        Category category = Category.builder()
                .id(jpaEntity.getCategory().getId())
                .name(jpaEntity.getCategory().getName())
                .createdAt(jpaEntity.getCategory().getCreatedAt())
                .updatedAt(jpaEntity.getCategory().getUpdatedAt())
                .build();

        return Product.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .description(jpaEntity.getDescription())
                .price(jpaEntity.getPrice())
                .available(jpaEntity.getAvailable())
                .category(category)
                .createdAt(jpaEntity.getCreatedAt())
                .updatedAt(jpaEntity.getUpdatedAt())
                .build();
    }
}
