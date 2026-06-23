package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.PageOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductInput;
import github.com.diegogrlima.gocoffe.application.dto.product.CreateProductOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.GetAllProductOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.GetProductByIdOutput;
import github.com.diegogrlima.gocoffe.domain.product.usecase.CreateProductUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.GetAllProductUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.GetProductByIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductUseCase getAllProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;

    @PostMapping
    public ResponseEntity<CreateProductOutput> create(@Valid @RequestBody CreateProductInput input) {
        CreateProductOutput output = createProductUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping
    public ResponseEntity<PageOutput<GetAllProductOutput>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageOutput<GetAllProductOutput> output = getAllProductUseCase.execute(page, size);
        return ResponseEntity.ok(output);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductByIdOutput> findById(@PathVariable UUID id) {
        GetProductByIdOutput output = getProductByIdUseCase.execute(id);
        return ResponseEntity.ok(output);
    }
}
