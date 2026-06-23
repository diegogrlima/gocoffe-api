package github.com.diegogrlima.gocoffe.domain.order.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private UUID id;

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than zero")
    private BigDecimal priceUnit;

    @NotNull(message = "Subtotal is required")
    @PositiveOrZero(message = "Subtotal must be zero or greater")
    private BigDecimal subtotal;
}
