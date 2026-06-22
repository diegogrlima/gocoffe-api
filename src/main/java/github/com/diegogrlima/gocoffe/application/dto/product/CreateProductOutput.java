package github.com.diegogrlima.gocoffe.application.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductOutput(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Boolean available,
        UUID categoryId
) {
}
