package github.com.diegogrlima.gocoffe.domain.category.usecase;

import github.com.diegogrlima.gocoffe.application.dto.category.UpdateCategoryInput;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCategoryByIdUseCase {

    private final CategoryRepository categoryRepository;

    public void execute(UpdateCategoryInput input) {
        Category category = categoryRepository.findById(input.id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(input.name());

        categoryRepository.UpdateCategory(category);
    }
}
