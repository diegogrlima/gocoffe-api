package github.com.diegogrlima.gocoffe.domain.category.usecase;

import github.com.diegogrlima.gocoffe.application.dto.category.CreateCategoryInput;
import github.com.diegogrlima.gocoffe.application.dto.category.CreateCategoryOutput;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CreateCategoryUseCase createCategoryUseCase;

  @Test
  void shouldCreateCategoryWithName() {
    String name = "Bebidas";
    CreateCategoryInput input = new CreateCategoryInput(name);

    Category savedCategory = Category.builder()
        .name(name)
        .build();

    when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

    CreateCategoryOutput result = createCategoryUseCase.execute(input);

    assertNotNull(result);
    assertEquals(name, result.name());
    verify(categoryRepository).save(any(Category.class));
  }

  @Test
  void shouldCallRepositorySaveOnce() {
    String name = "Sobremesas";
    CreateCategoryInput input = new CreateCategoryInput(name);

    Category savedCategory = Category.builder()
        .name(name)
        .build();

    when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

    createCategoryUseCase.execute(input);

    verify(categoryRepository).save(any(Category.class));
  }

  @Test
  void shouldNotCreateCategoryWhenNameAlreadyExists() {
    String name = "Bebidas";
    CreateCategoryInput input = new CreateCategoryInput(name);

    Category existingCategory = Category.builder()
        .name(name)
        .build();

    when(categoryRepository.findByName(name))
        .thenReturn(Optional.of(existingCategory));

    var exception = assertThrows(
        RuntimeException.class,
        () -> createCategoryUseCase.execute(input));

    assertEquals(
        "Category already exists",
        exception.getMessage());

    verify(categoryRepository)
        .findByName(name);

    verify(categoryRepository, never())
        .save(any(Category.class));
  }
}
