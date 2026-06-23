package github.com.diegogrlima.gocoffe.domain.category.usecase;

import github.com.diegogrlima.gocoffe.config.exception.ResourceNotFoundException;
import github.com.diegogrlima.gocoffe.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCategoryByIdUseCase {

    private final CategoryRepository categoryRepository;

    public void execute(UUID id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.deleteById(id);
    }
}
