package github.com.diegogrlima.gocoffe.domain.product.repository;

import github.com.diegogrlima.gocoffe.domain.product.entity.ProductImage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository {

    ProductImage save(ProductImage productImage);

    Optional<ProductImage> findById(UUID id);

    List<ProductImage> findByProductId(UUID productId);

    void deleteById(UUID id);
}
