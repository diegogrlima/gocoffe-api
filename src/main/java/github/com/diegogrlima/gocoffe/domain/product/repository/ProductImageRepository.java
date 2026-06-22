package github.com.diegogrlima.gocoffe.domain.product.repository;

import github.com.diegogrlima.gocoffe.domain.product.entity.ProductImage;

import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository {

    ProductImage save(ProductImage productImage);

    Optional<ProductImage> findById(UUID id);

    void deleteById(UUID id);
}
