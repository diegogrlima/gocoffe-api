package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.PageOutput;
import github.com.diegogrlima.gocoffe.application.dto.product.*;
import github.com.diegogrlima.gocoffe.domain.product.usecase.CreateProductUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.DeleteProductByIdUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.GetAllProductUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.GetProductByIdUseCase;
import github.com.diegogrlima.gocoffe.domain.product.usecase.UpdateProductByIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductUseCase getAllProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final UpdateProductByIdUseCase updateProductByIdUseCase;
    private final DeleteProductByIdUseCase deleteProductByIdUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateProductInput input) {
        UpdateProductInput updateInput = new UpdateProductInput(
                id,
                input.name(),
                input.description(),
                input.price(),
                input.categoryId()
        );
        updateProductByIdUseCase.execute(updateInput);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProductByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
