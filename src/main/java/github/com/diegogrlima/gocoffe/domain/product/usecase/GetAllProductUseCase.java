package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.PageOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.GetAllProductOutput;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllProductUseCase {

    private final ProductRepository productRepository;

    public PageOutput<GetAllProductOutput> execute(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<Product> productPage = productRepository.findAll(pageable);

        java.util.List<GetAllProductOutput> content = productPage.getContent().stream()
                .map(product -> new GetAllProductOutput(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getAvailable(),
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCreatedAt()
                ))
                .toList();

        return new PageOutput<>(
                content,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }
}
