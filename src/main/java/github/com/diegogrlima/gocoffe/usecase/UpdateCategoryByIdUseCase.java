package github.com.diegogrlima.gocoffe.usecase;

import github.com.diegogrlima.gocoffe.dto.UpdateCategoryInput;
import github.com.diegogrlima.gocoffe.entity.Category;
import github.com.diegogrlima.gocoffe.repository.CategoryRepository;
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
