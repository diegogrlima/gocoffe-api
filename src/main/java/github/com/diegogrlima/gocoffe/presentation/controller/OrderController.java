package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderInput;
import github.com.diegogrlima.gocoffe.application.dto.order.CreateOrderOutput;
import github.com.diegogrlima.gocoffe.application.dto.order.GetOrderByIdOutput;
import github.com.diegogrlima.gocoffe.application.dto.order.GetOrderMetricsOutput;
import github.com.diegogrlima.gocoffe.application.dto.order.UpdateOrderStatusInput;
import github.com.diegogrlima.gocoffe.domain.order.usecase.CreateOrderUseCase;
import github.com.diegogrlima.gocoffe.domain.order.usecase.GetOrderByIdUseCase;
import github.com.diegogrlima.gocoffe.domain.order.usecase.GetOrderMetricsUseCase;
import github.com.diegogrlima.gocoffe.domain.order.usecase.UpdateOrderStatusUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final GetOrderMetricsUseCase getOrderMetricsUseCase;

    @PostMapping
    public ResponseEntity<CreateOrderOutput> create(@Valid @RequestBody CreateOrderInput input) {
        CreateOrderOutput output = createOrderUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOrderByIdOutput> findById(@PathVariable UUID id) {
        GetOrderByIdOutput output = getOrderByIdUseCase.execute(id);
        return ResponseEntity.ok(output);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateOrderStatusInput input) {
        UpdateOrderStatusInput updateInput = new UpdateOrderStatusInput(id, input.status());
        updateOrderStatusUseCase.execute(updateInput);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetOrderMetricsOutput> getMetrics() {
        GetOrderMetricsOutput output = getOrderMetricsUseCase.execute();
        return ResponseEntity.ok(output);
    }
}
