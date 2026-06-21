package github.com.diegogrlima.gocoffe.repository;

import github.com.diegogrlima.gocoffe.entity.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findByName(String name);
    Optional<Category> findById(UUID id);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
