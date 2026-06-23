package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.product.entity.Product;
import github.com.diegogrlima.gocoffe.domain.product.entity.ProductImage;
import github.com.diegogrlima.gocoffe.domain.product.repository.ImageStorageRepository;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductImageRepository;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProductByIdUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ImageStorageRepository imageStorageRepository;

    @InjectMocks
    private DeleteProductByIdUseCase deleteProductByIdUseCase;

    @Test
    void shouldDeleteProductWhenExists() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

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
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productImageRepository.findAllByProductId(productId)).thenReturn(List.of());

        deleteProductByIdUseCase.execute(productId);

        verify(productRepository).findById(productId);
        verify(productImageRepository).findAllByProductId(productId);
        verify(productImageRepository).deleteAllByProductId(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldDeleteProductWithImagesFromCloudinary() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID imageId = UUID.randomUUID();

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
                .build();

        ProductImage image = ProductImage.builder()
                .id(imageId)
                .imageURL("https://res.cloudinary.com/demo/image/upload/v1234567890/gocoffe/products/image.jpg")
                .productId(productId)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productImageRepository.findAllByProductId(productId)).thenReturn(List.of(image));

        deleteProductByIdUseCase.execute(productId);

        verify(productRepository).findById(productId);
        verify(productImageRepository).findAllByProductId(productId);
        verify(imageStorageRepository).delete("v1234567890/gocoffe/products/image");
        verify(productImageRepository).deleteAllByProductId(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldDeleteMultipleProductImagesFromCloudinary() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

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
                .build();

        ProductImage image1 = ProductImage.builder()
                .id(UUID.randomUUID())
                .imageURL("https://res.cloudinary.com/demo/image/upload/v1234567890/gocoffe/products/image1.jpg")
                .productId(productId)
                .build();

        ProductImage image2 = ProductImage.builder()
                .id(UUID.randomUUID())
                .imageURL("https://res.cloudinary.com/demo/image/upload/v1234567890/gocoffe/products/image2.png")
                .productId(productId)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productImageRepository.findAllByProductId(productId)).thenReturn(List.of(image1, image2));

        deleteProductByIdUseCase.execute(productId);

        verify(productRepository).findById(productId);
        verify(productImageRepository).findAllByProductId(productId);
        verify(imageStorageRepository).delete("v1234567890/gocoffe/products/image1");
        verify(imageStorageRepository).delete("v1234567890/gocoffe/products/image2");
        verify(productImageRepository).deleteAllByProductId(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> deleteProductByIdUseCase.execute(productId));

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, never()).deleteById(productId);
        verify(productImageRepository, never()).findAllByProductId(productId);
        verify(imageStorageRepository, never()).delete(org.mockito.ArgumentMatchers.anyString());
    }
}
