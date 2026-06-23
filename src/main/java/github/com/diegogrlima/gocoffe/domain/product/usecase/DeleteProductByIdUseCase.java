package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.domain.product.entity.ProductImage;
import github.com.diegogrlima.gocoffe.domain.product.repository.ImageStorageRepository;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductImageRepository;
import github.com.diegogrlima.gocoffe.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductByIdUseCase {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageStorageRepository imageStorageRepository;

    public void execute(UUID id) {
        productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductImage> images = productImageRepository.findAllByProductId(id);
        for (ProductImage image : images) {
            String publicId = extractPublicId(image.getImageURL());
            imageStorageRepository.delete(publicId);
        }

        productImageRepository.deleteAllByProductId(id);
        productRepository.deleteById(id);
    }

    private String extractPublicId(String imageUrl) {
        String[] parts = imageUrl.split("/");
        int uploadIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("upload")) {
                uploadIndex = i;
                break;
            }
        }
        if (uploadIndex == -1 || uploadIndex + 1 >= parts.length) {
            throw new RuntimeException("Invalid Cloudinary URL");
        }
        StringBuilder publicId = new StringBuilder();
        for (int i = uploadIndex + 1; i < parts.length; i++) {
            if (i > uploadIndex + 1) {
                publicId.append("/");
            }
            publicId.append(parts[i]);
        }
        String lastPart = publicId.toString();
        int dotIndex = lastPart.lastIndexOf(".");
        if (dotIndex > 0) {
            publicId = new StringBuilder(lastPart.substring(0, dotIndex));
        }
        return publicId.toString();
    }
}
