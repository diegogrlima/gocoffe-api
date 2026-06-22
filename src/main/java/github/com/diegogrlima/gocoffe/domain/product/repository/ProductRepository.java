package github.com.diegogrlima.gocoffe.domain.product.repository;

import github.com.diegogrlima.gocoffe.domain.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    Optional<Product> findByName(String name);

    List<Product> findAll();

    List<Product> findByCategoryId(UUID categoryId);

    void deleteById(UUID id);
}
