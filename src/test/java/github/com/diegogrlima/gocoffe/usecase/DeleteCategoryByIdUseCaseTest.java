package github.com.diegogrlima.gocoffe.usecase;

import github.com.diegogrlima.gocoffe.entity.Category;
import github.com.diegogrlima.gocoffe.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryByIdUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private DeleteCategoryByIdUseCase deleteCategoryByIdUseCase;

    @Test
    void shouldDeleteCategoryWhenExists() {
        UUID id = UUID.randomUUID();

        Category existingCategory = Category.builder()
                .id(id)
                .name("Bebidas")
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));

        deleteCategoryByIdUseCase.execute(id);

        verify(categoryRepository).findById(id);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> deleteCategoryByIdUseCase.execute(id));

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).deleteById(any(UUID.class));
    }
}
