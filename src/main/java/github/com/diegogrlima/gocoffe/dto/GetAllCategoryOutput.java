package github.com.diegogrlima.gocoffe.dto;

import java.util.UUID;

public record GetAllCategoryOutput(
        UUID id,
        String name
) {
}
