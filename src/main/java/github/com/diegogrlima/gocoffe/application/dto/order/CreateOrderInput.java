package github.com.diegogrlima.gocoffe.application.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderInput(
        @NotBlank(message = "Customer CPF is required")
        @Size(min = 11, max = 14, message = "CPF must be between 11 and 14 characters")
        String customerCpf,

        @NotEmpty(message = "Order items are required")
        @Valid
        List<OrderItemInput> items
) {
    public record OrderItemInput(
            @NotNull(message = "Product ID is required")
            UUID productId,

            @NotNull(message = "Quantity is required")
            @Min(value = 1, message = "Quantity must be at least 1")
            Integer quantity,

            @NotNull(message = "Unit price is required")
            @Positive(message = "Unit price must be greater than zero")
            BigDecimal priceUnit
    ) {
    }
}
