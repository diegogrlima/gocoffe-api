package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.UploadImageOutput;
import github.com.diegogrlima.gocoffe.domain.product.repository.ImageStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadProductImageUseCase {

    private final ImageStorageRepository imageStorageRepository;

    public UploadImageOutput execute(MultipartFile file) {
        String url = imageStorageRepository.upload(file);
        return new UploadImageOutput(url, null, null, 0, 0);
    }
}
