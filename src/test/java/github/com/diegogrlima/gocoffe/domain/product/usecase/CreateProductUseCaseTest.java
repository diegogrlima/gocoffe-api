package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductInput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductOutput;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.category.repository.CategoryRepository;
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

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    @Test
    void shouldCreateProductWhenValidInput() {
        UUID categoryId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                "Cafe Especial", "Cafe torrado", new BigDecimal("25.00"), categoryId);

        Category category = Category.builder()
                .id(categoryId)
                .name("Bebidas")
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findByName("Cafe Especial")).thenReturn(Optional.empty());

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

        Category category = Category.builder()
                .id(categoryId)
                .name("Bebidas")
                .build();

        Product existingProduct = Product.builder()
                .name("Cafe Especial")
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findByName("Cafe Especial")).thenReturn(Optional.of(existingProduct));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Product already exists", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        UUID categoryId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                null, "Cafe torrado", new BigDecimal("25.00"), categoryId);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Name is required", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        UUID categoryId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                "", "Cafe torrado", new BigDecimal("25.00"), categoryId);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Name is required", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsNull() {
        CreateProductInput input = new CreateProductInput(
                "Cafe Especial", "Cafe torrado", new BigDecimal("25.00"), null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Category is required", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        UUID categoryId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                "Cafe Especial", "Cafe torrado", new BigDecimal("25.00"), categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Category not found", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        UUID categoryId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                "Cafe Especial", "Cafe torrado", new BigDecimal("-10.00"), categoryId);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Price must be greater than zero", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        UUID categoryId = UUID.randomUUID();

        CreateProductInput input = new CreateProductInput(
                "Cafe Especial", "Cafe torrado", null, categoryId);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createProductUseCase.execute(input));

        assertEquals("Price is required", exception.getMessage());
        verify(productRepository, never()).save(any());
    }
}
