package github.com.diegogrlima.gocoffe.domain.product.repository;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageRepository {

    String upload(MultipartFile file);

    void delete(String publicId);
}
