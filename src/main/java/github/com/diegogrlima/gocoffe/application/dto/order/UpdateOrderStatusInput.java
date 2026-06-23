package github.com.diegogrlima.gocoffe.application.dto.order;

import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateOrderStatusInput(
        UUID id,

        @NotNull(message = "Status is required")
        OrderStatus status
) {
}
