package github.com.diegogrlima.gocoffe.application.dto.order;

import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetAllOrderOutput(
        UUID id,
        String orderCode,
        String customerCpf,
        OrderStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) {
}
