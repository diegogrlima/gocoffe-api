package github.com.diegogrlima.gocoffe.usecase;

import github.com.diegogrlima.gocoffe.dto.UpdateCategoryInput;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryByIdUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private UpdateCategoryByIdUseCase updateCategoryByIdUseCase;

    @Test
    void shouldUpdateCategoryWhenExists() {
        UUID id = UUID.randomUUID();
        String newName = "Bebidas Atualizado";

        Category existingCategory = Category.builder()
                .id(id)
                .name("Bebidas")
                .build();

        UpdateCategoryInput input = new UpdateCategoryInput(id, newName);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));

        updateCategoryByIdUseCase.execute(input);

        verify(categoryRepository).findById(id);
        verify(categoryRepository).UpdateCategory(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        UUID id = UUID.randomUUID();
        String newName = "Bebidas Atualizado";

        UpdateCategoryInput input = new UpdateCategoryInput(id, newName);

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> updateCategoryByIdUseCase.execute(input));

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).save(any(Category.class));
    }
}
