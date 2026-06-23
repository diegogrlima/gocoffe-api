package github.com.diegogrlima.gocoffe.application.dto.order;

import github.com.diegogrlima.gocoffe.domain.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderOutput(
        UUID id,
        String orderCode,
        String customerCpf,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderItemOutput> items,
        LocalDateTime createdAt
) {
    public record OrderItemOutput(
            UUID id,
            UUID productId,
            Integer quantity,
            BigDecimal priceUnit,
            BigDecimal subtotal
    ) {
    }
}
