package github.com.diegogrlima.gocoffe.domain.product.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    private UUID id;

    @NotBlank(message = "Image URL is required")
    private String imageURL;

    private LocalDateTime createdAt;
}
