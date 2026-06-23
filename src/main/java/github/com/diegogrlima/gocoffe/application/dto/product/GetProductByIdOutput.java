package github.com.diegogrlima.gocoffe.application.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetProductByIdOutput(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Boolean available,
        UUID categoryId,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
