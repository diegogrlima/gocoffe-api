package github.com.diegogrlima.gocoffe.domain.category.usecase;

import github.com.diegogrlima.gocoffe.application.dto.category.CreateCategoryInput;
import github.com.diegogrlima.gocoffe.application.dto.category.CreateCategoryOutput;
import github.com.diegogrlima.gocoffe.domain.category.entity.Category;
import github.com.diegogrlima.gocoffe.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public CreateCategoryOutput execute(CreateCategoryInput input) {
        var categoryExists = categoryRepository.findByName(input.name());

        if (categoryExists.isPresent()) {
            throw new RuntimeException("Category already exists");
        }

        Category category = Category.builder()
                .name(input.name())
                .build();

        Category savedCategory = categoryRepository.save(category);

        return new CreateCategoryOutput(
                savedCategory.getId(),
                savedCategory.getName()
        );
    }
}
