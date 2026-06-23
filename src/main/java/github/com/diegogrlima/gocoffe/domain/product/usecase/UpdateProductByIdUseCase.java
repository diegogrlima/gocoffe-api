package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.UpdateProductInput;
import github.com.diegogrlima.gocoffe.config.exception.ResourceNotFoundException;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductByIdUseCase {

    private final ProductRepository productRepository;

    public void execute(UpdateProductInput input) {
        Product product = productRepository.findById(input.id())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = Category.builder()
                .id(input.categoryId())
                .build();

        product.setName(input.name());
        product.setDescription(input.description());
        product.setPrice(input.price());
        product.setCategory(category);

        productRepository.save(product);
    }
}
