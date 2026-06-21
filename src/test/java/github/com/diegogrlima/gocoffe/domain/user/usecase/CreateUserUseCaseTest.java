package github.com.diegogrlima.gocoffe.domain.user.usecase;

import github.com.diegogrlima.gocoffe.application.dto.user.CreateUserInput;
import github.com.diegogrlima.gocoffe.application.dto.user.CreateUserOutput;
import github.com.diegogrlima.gocoffe.domain.user.entity.Role;
import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void shouldCreateUserWhenAdminAndValidInput() {
        CreateUserInput input = new CreateUserInput(
                "Diego", "diego@test.com", "123456", Role.USER);

        when(userRepository.findByEmail("diego@test.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456"))
                .thenReturn("$2a$10$encodedPassword");

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name("Diego")
                .email("diego@test.com")
                .role(Role.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        CreateUserOutput result = createUserUseCase.execute(input, Role.ADMIN);

        assertNotNull(result);
        assertEquals("Diego", result.name());
        assertEquals("diego@test.com", result.email());
        assertEquals(Role.USER, result.role());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenNonAdminTriesToCreateUser() {
        CreateUserInput input = new CreateUserInput(
                "Diego", "diego@test.com", "123456", Role.USER);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createUserUseCase.execute(input, Role.USER));

        assertEquals("Only ADMIN users can create new users", exception.getMessage());
        verify(userRepository, never()).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTryingToCreateAdminUser() {
        CreateUserInput input = new CreateUserInput(
                "Admin2", "admin2@test.com", "123456", Role.ADMIN);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createUserUseCase.execute(input, Role.ADMIN));

        assertEquals("Cannot create user with ADMIN role", exception.getMessage());
        verify(userRepository, never()).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        CreateUserInput input = new CreateUserInput(
                "Diego", "diego@test.com", "123456", Role.USER);

        User existingUser = User.builder()
                .email("diego@test.com")
                .build();

        when(userRepository.findByEmail("diego@test.com"))
                .thenReturn(Optional.of(existingUser));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> createUserUseCase.execute(input, Role.ADMIN));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
