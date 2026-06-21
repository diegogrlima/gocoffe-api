package github.com.diegogrlima.gocoffe.usecase;

import github.com.diegogrlima.gocoffe.dto.CreateCategoryInput;
import github.com.diegogrlima.gocoffe.dto.CreateCategoryOutput;
import github.com.diegogrlima.gocoffe.entity.Category;
import github.com.diegogrlima.gocoffe.repository.CategoryRepository;
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
