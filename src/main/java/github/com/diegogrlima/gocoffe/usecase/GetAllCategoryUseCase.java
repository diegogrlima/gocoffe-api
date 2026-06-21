package github.com.diegogrlima.gocoffe.usecase;

import github.com.diegogrlima.gocoffe.dto.GetAllCategoryOutput;
import github.com.diegogrlima.gocoffe.repository.CategoryRepository;
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
