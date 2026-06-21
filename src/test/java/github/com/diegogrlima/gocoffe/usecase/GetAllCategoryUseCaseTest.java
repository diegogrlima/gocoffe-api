package github.com.diegogrlima.gocoffe.usecase;

import github.com.diegogrlima.gocoffe.dto.GetAllCategoryOutput;
import github.com.diegogrlima.gocoffe.entity.Category;
import github.com.diegogrlima.gocoffe.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCategoryUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GetAllCategoryUseCase getAllCategoryUseCase;

    @Test
    void shouldReturnAllCategories() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Category category1 = Category.builder()
                .id(id1)
                .name("Bebidas")
                .build();

        Category category2 = Category.builder()
                .id(id2)
                .name("Sobremesas")
                .build();

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<GetAllCategoryOutput> result = getAllCategoryUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bebidas", result.get(0).name());
        assertEquals("Sobremesas", result.get(1).name());
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<GetAllCategoryOutput> result = getAllCategoryUseCase.execute();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(categoryRepository).findAll();
    }
}
