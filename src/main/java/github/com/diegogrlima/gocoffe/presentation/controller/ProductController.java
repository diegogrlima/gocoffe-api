package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductInput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductOutput;
import github.com.diegogrlima.gocoffe.domain.product.usecase.CreateProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;

    @PostMapping
    public ResponseEntity<CreateProductOutput> create(@Valid @RequestBody CreateProductInput input) {
        CreateProductOutput output = createProductUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
