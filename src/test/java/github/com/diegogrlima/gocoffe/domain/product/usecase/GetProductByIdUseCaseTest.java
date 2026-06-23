package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.GetProductByIdOutput;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductByIdUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductByIdUseCase getProductByIdUseCase;

    @Test
    void shouldReturnProductWhenFound() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Category category = Category.builder()
                .id(categoryId)
                .name("Bebidas")
                .build();

        Product product = Product.builder()
                .id(productId)
                .name("Cafe Especial")
                .description("Cafe torrado")
                .price(new BigDecimal("25.00"))
                .available(true)
                .category(category)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        GetProductByIdOutput result = getProductByIdUseCase.execute(productId);

        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals("Cafe Especial", result.name());
        assertEquals("Cafe torrado", result.description());
        assertEquals(new BigDecimal("25.00"), result.price());
        assertEquals(true, result.available());
        assertEquals(categoryId, result.categoryId());
        assertEquals("Bebidas", result.categoryName());
        assertNotNull(result.createdAt());
        assertNotNull(result.updatedAt());

        verify(productRepository).findById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getProductByIdUseCase.execute(productId));

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository).findById(productId);
    }
}
