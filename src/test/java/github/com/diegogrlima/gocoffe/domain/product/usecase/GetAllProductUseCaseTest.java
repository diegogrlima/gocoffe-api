package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.PageOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.GetAllProductOutput;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetAllProductUseCase getAllProductUseCase;

    @Test
    void shouldReturnPaginatedProducts() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Category category = Category.builder()
                .id(categoryId)
                .name("Bebidas")
                .build();

        Product product1 = Product.builder()
                .id(productId1)
                .name("Cafe Especial")
                .description("Cafe torrado")
                .price(new BigDecimal("25.00"))
                .available(true)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        Product product2 = Product.builder()
                .id(productId2)
                .name("Cha Verde")
                .description("Cha organico")
                .price(new BigDecimal("15.00"))
                .available(true)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        Page<Product> productPage = new PageImpl<>(
                List.of(product1, product2),
                PageRequest.of(0, 10, Sort.by("name").ascending()),
                2
        );

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        PageOutput<GetAllProductOutput> result = getAllProductUseCase.execute(0, 10);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());

        GetAllProductOutput output1 = result.content().get(0);
        assertEquals(productId1, output1.id());
        assertEquals("Cafe Especial", output1.name());
        assertEquals("Cafe torrado", output1.description());
        assertEquals(new BigDecimal("25.00"), output1.price());
        assertEquals(true, output1.available());
        assertEquals(categoryId, output1.categoryId());
        assertEquals("Bebidas", output1.categoryName());
        assertNotNull(output1.createdAt());

        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnEmptyPageWhenNoProducts() {
        Page<Product> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10, Sort.by("name").ascending()),
                0
        );

        when(productRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        PageOutput<GetAllProductOutput> result = getAllProductUseCase.execute(0, 10);

        assertNotNull(result);
        assertEquals(0, result.content().size());
        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());

        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnCorrectPageMetadata() {
        UUID categoryId = UUID.randomUUID();

        Category category = Category.builder()
                .id(categoryId)
                .name("Bebidas")
                .build();

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Cafe Especial")
                .description("Cafe torrado")
                .price(new BigDecimal("25.00"))
                .available(true)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        Page<Product> productPage = new PageImpl<>(
                List.of(product),
                PageRequest.of(2, 5, Sort.by("name").ascending()),
                15
        );

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        PageOutput<GetAllProductOutput> result = getAllProductUseCase.execute(2, 5);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(2, result.page());
        assertEquals(5, result.size());
        assertEquals(15, result.totalElements());
        assertEquals(3, result.totalPages());

        verify(productRepository).findAll(any(Pageable.class));
    }
}
