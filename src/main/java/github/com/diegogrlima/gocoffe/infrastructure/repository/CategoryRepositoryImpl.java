package github.com.diegogrlima.gocoffe.infrastructure.repository;

import github.com.diegogrlima.gocoffe.entity.Category;
import github.com.diegogrlima.gocoffe.infrastructure.jpa.CategoryJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.jpa.CategoryJpaRepository;
import github.com.diegogrlima.gocoffe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity jpaEntity = toJpaEntity(category);
        CategoryJpaEntity savedEntity = categoryJpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name)
                .map(this::toDomainEntity);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        categoryJpaRepository.deleteById(id);
    }

    private CategoryJpaEntity toJpaEntity(Category category) {
        return CategoryJpaEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private Category toDomainEntity(CategoryJpaEntity jpaEntity) {
        return Category.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .createdAt(jpaEntity.getCreatedAt())
                .updatedAt(jpaEntity.getUpdatedAt())
                .build();
    }
}
