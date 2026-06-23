package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.UpdateProductInput;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductByIdUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UpdateProductByIdUseCase updateProductByIdUseCase;

    @Test
    void shouldUpdateProductWhenExists() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Category category = Category.builder()
                .id(categoryId)
                .name("Bebidas")
                .build();

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Cafe Especial")
                .description("Cafe torrado")
                .price(new BigDecimal("25.00"))
                .available(true)
                .category(category)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        UpdateProductInput input = new UpdateProductInput(
                productId,
                "Cafe Atualizado",
                "Cafe premium",
                new BigDecimal("35.00"),
                categoryId
        );

        updateProductByIdUseCase.execute(input);

        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        UpdateProductInput input = new UpdateProductInput(
                productId,
                "Cafe Atualizado",
                "Cafe premium",
                new BigDecimal("35.00"),
                categoryId
        );

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> updateProductByIdUseCase.execute(input));

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, never()).save(any());
    }
}
