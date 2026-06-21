package github.com.diegogrlima.gocoffe.domain.category.usecase;

import github.com.diegogrlima.gocoffe.application.dto.category.GetAllCategoryOutput;
import github.com.diegogrlima.gocoffe.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public List<GetAllCategoryOutput> execute() {
        return categoryRepository.findAll().stream()
                .map(category -> new GetAllCategoryOutput(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }
}
