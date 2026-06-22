package github.com.diegogrlima.gocoffe.application.dto;

import java.util.List;

public record PageOutput<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
