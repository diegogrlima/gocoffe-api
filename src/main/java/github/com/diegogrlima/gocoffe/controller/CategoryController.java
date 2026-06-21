package github.com.diegogrlima.gocoffe.controller;

import github.com.diegogrlima.gocoffe.dto.CreateCategoryInput;
import github.com.diegogrlima.gocoffe.dto.CreateCategoryOutput;
import github.com.diegogrlima.gocoffe.usecase.CreateCategoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;

    @PostMapping
    public ResponseEntity<CreateCategoryOutput> create(@Valid @RequestBody CreateCategoryInput input) {
        CreateCategoryOutput output = createCategoryUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
