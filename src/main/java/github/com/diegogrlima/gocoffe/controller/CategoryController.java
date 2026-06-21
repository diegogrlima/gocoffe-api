package github.com.diegogrlima.gocoffe.controller;

import github.com.diegogrlima.gocoffe.dto.CreateCategoryInput;
import github.com.diegogrlima.gocoffe.dto.CreateCategoryOutput;
import github.com.diegogrlima.gocoffe.dto.GetAllCategoryOutput;
import github.com.diegogrlima.gocoffe.dto.UpdateCategoryInput;
import github.com.diegogrlima.gocoffe.usecase.CreateCategoryUseCase;
import github.com.diegogrlima.gocoffe.usecase.GetAllCategoryUseCase;
import github.com.diegogrlima.gocoffe.usecase.DeleteCategoryByIdUseCase;
import github.com.diegogrlima.gocoffe.usecase.UpdateCategoryByIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetAllCategoryUseCase getAllCategoryUseCase;
    private final UpdateCategoryByIdUseCase updateCategoryByIdUseCase;
    private final DeleteCategoryByIdUseCase deleteCategoryByIdUseCase;

    @PostMapping
    public ResponseEntity<CreateCategoryOutput> create(@Valid @RequestBody CreateCategoryInput input) {
        CreateCategoryOutput output = createCategoryUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping
    public ResponseEntity<List<GetAllCategoryOutput>> findAll() {
        List<GetAllCategoryOutput> categories = getAllCategoryUseCase.execute();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryInput input) {
        UpdateCategoryInput updateInput = new UpdateCategoryInput(id, input.name());
        updateCategoryByIdUseCase.execute(updateInput);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCategoryByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
