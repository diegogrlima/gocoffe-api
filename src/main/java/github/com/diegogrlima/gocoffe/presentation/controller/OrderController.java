package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderInput;
import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderOutput;
import github.com.diegogrlima.gocoffe.domain.order.usecase.CreateOrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    @PostMapping
    public ResponseEntity<CreateOrderOutput> create(@Valid @RequestBody CreateOrderInput input) {
        CreateOrderOutput output = createOrderUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
