package github.com.diegogrlima.gocoffe.application.dto.order;

import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;

import java.util.UUID;

public record GetOrderByIdOutput(
        UUID id,
        String orderCode,
        OrderStatus status
) {
}
