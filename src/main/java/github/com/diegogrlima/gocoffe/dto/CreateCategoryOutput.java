package github.com.diegogrlima.gocoffe.dto;

import java.util.UUID;

public record CreateCategoryOutput(
        UUID id,
        String name
) {
}
