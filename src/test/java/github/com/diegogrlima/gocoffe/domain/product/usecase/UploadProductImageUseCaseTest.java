package github.com.diegogrlima.gocoffe.domain.product.usecase;

import github.com.diegogrlima.gocoffe.application.dto.product.UploadImageOutput;
import github.com.diegogrlima.gocoffe.domain.product.repository.ImageStorageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadProductImageUseCaseTest {

    @Mock
    private ImageStorageRepository imageStorageRepository;

    @InjectMocks
    private UploadProductImageUseCase uploadProductImageUseCase;

    @Test
    void shouldUploadImageSuccessfully() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        String expectedUrl = "https://res.cloudinary.com/demo/image/upload/v1234567890/gocoffe/products/image.jpg";
        when(imageStorageRepository.upload(any())).thenReturn(expectedUrl);

        UploadImageOutput result = uploadProductImageUseCase.execute(file);

        assertNotNull(result);
        assertEquals(expectedUrl, result.url());
        verify(imageStorageRepository).upload(file);
    }
}
