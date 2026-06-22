package github.com.diegogrlima.gocoffe.application.dto.product;

import java.util.List;

public record PageOutput<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
