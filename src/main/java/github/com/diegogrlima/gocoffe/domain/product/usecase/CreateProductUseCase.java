package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductInput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductOutput;
import github.com.diegogrlima.gocoffe.config.exception.ResourceAlreadyExistsException;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductOutput execute(CreateProductInput input) {
        productRepository.findByName(input.name())
                .ifPresent(product -> {
                    throw new ResourceAlreadyExistsException("Product already exists");
                });

        Product product = Product.builder()
                .name(input.name())
                .description(input.description())
                .price(input.price())
                .available(true)
                .build();

        Product savedProduct = productRepository.save(product);

        return new CreateProductOutput(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getAvailable(),
                savedProduct.getCategory().getId()
        );
    }
}
