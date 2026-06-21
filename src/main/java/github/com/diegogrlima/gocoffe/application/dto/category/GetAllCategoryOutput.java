package github.com.diegogrlima.gocoffe.application.dto.category;

import java.util.UUID;

public record GetAllCategoryOutput(
        UUID id,
        String name
) {
}
