package github.com.diegogrlima.gocoffe.application.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductInput(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Category is required")
        UUID categoryId
) {
}
