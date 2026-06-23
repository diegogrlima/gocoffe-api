package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductByIdUseCase {

    private final ProductRepository productRepository;

    public void execute(UUID id) {
        productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.deleteById(id);
    }
}
