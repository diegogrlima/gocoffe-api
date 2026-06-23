package github.com.diegogrlima.gocoffe.application.dto.product;

public record UploadImageOutput(
        String url,
        String publicId,
        String format,
        int width,
        int height
) {}
