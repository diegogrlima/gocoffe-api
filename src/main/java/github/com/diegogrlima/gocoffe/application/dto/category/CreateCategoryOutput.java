package github.com.diegogrlima.gocoffe.application.dto.category;

import java.util.UUID;

public record CreateCategoryOutput(
        UUID id,
        String name
) {
}
