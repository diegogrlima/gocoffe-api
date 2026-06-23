package github.com.diegogrlima.gocoffe.domain.order.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID id;

    @NotBlank(message = "Order code is required")
    @Size(max = 50, message = "Order code must not exceed 50 characters")
    private String orderCode;

    @NotBlank(message = "Customer CPF is required")
    @Size(min = 11, max = 14, message = "CPF must be between 11 and 14 characters")
    private String customerCpf;

    @NotNull(message = "Status is required")
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    public BigDecimal calculateTotalPrice() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
