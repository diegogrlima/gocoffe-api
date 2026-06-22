package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductInput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductOutput;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    @Test
    void shouldCreateProductWhenValidInput() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                "Cafe Especial", "Cafe torrado", new BigDecimal("25.00"), categoryId);

        when(productRepository.findByName("Cafe Especial")).thenReturn(Optional.empty());

        Category category = Category.builder()
                .id(categoryId)
                .name("Bebidas")
                .build();

        Product savedProduct = Product.builder()
                .id(productId)
                .name("Cafe Especial")
                .description("Cafe torrado")
                .price(new BigDecimal("25.00"))
                .available(true)
                .category(category)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        CreateProductOutput result = createProductUseCase.execute(input);

        assertNotNull(result);
        assertEquals("Cafe Especial", result.name());
        assertEquals(new BigDecimal("25.00"), result.price());
        assertEquals(categoryId, result.categoryId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        UUID categoryId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                "Cafe Especial", "Cafe torrado", new BigDecimal("25.00"), categoryId);

        Product existingProduct = Product.builder()
                .name("Cafe Especial")
                .build();

        when(productRepository.findByName("Cafe Especial")).thenReturn(Optional.of(existingProduct));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Product already exists", exception.getMessage());
        verify(productRepository, never()).save(any());
    }
}
