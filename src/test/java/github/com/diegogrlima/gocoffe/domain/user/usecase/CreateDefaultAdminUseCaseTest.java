package github.com.diegogrlima.gocoffe.domain.user.usecase;

import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateDefaultAdminUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateDefaultAdminUseCase createDefaultAdminUseCase;

    @Test
    void shouldCreateAdminWhenNotExists() {
        when(userRepository.findByEmail("admin@gocoffe.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("admin123"))
                .thenReturn("$2a$10$encodedPassword");

        createDefaultAdminUseCase.execute();

        verify(userRepository).findByEmail("admin@gocoffe.com");
        verify(passwordEncoder).encode("admin123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenAdminAlreadyExists() {
        User existingAdmin = User.builder()
                .name("Admin")
                .email("admin@gocoffe.com")
                .build();

        when(userRepository.findByEmail("admin@gocoffe.com"))
                .thenReturn(Optional.of(existingAdmin));

        assertThrows(RuntimeException.class,
                () -> createDefaultAdminUseCase.execute());

        verify(userRepository).findByEmail("admin@gocoffe.com");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
