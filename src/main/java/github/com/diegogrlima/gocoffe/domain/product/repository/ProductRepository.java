package github.com.diegogrlima.gocoffe.domain.product.repository;

import github.com.diegogrlima.gocoffe.domain.product.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    Optional<Product> findByName(String name);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

    List<Product> findByCategoryId(UUID categoryId);

    void deleteById(UUID id);
}
